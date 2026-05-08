using MediatR;
using Microsoft.Extensions.Logging;

namespace PipelineDemo.Behaviors;

public class LoggingBehavior<TRequest, TResponse>
    : IPipelineBehavior<TRequest, TResponse>
    where TRequest : IRequest<TResponse>
{
    private readonly ILogger<LoggingBehavior<TRequest, TResponse>> _logger;

    public LoggingBehavior(ILogger<LoggingBehavior<TRequest, TResponse>> logger)
    {
        _logger = logger;
    }

    public async Task<TResponse> Handle(
        TRequest request,
        RequestHandlerDelegate<TResponse> next,
        CancellationToken cancellationToken)
    {
        var requestName = typeof(TRequest).Name;

        _logger.LogInformation(
            "📥 Request Başladı: [{RequestName}] | İçerik: {@Request}",
            requestName,
            request
        );

        TResponse response;

        try
        {
            response = await next();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "❌ Request Hata Verdi: [{RequestName}]", requestName);
            throw;
        }

        _logger.LogInformation(
            "📤 Request Tamamlandı: [{RequestName}] | Cevap: {@Response}",
            requestName,
            response
        );

        return response;
    }
}
