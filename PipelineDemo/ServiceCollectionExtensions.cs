using MediatR;
using Microsoft.Extensions.DependencyInjection;
using PipelineDemo.Behaviors;

namespace PipelineDemo;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddPipelineBehaviors(this IServiceCollection services)
    {
        services.AddMediatR(cfg =>
        {
            cfg.RegisterServicesFromAssembly(typeof(ServiceCollectionExtensions).Assembly);

            // Sıra önemli — pipeline soldan sağa çalışır
            cfg.AddBehavior(typeof(IPipelineBehavior<,>), typeof(LoggingBehavior<,>));
            cfg.AddBehavior(typeof(IPipelineBehavior<,>), typeof(PerformanceBehavior<,>));
            cfg.AddBehavior(typeof(IPipelineBehavior<,>), typeof(TransactionBehavior<,>));
        });

        return services;
    }
}
