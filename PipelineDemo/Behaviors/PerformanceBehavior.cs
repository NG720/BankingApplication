using System.Diagnostics;
using MediatR;
using Microsoft.Extensions.Logging;

namespace PipelineDemo.Behaviors;

public class PerformanceBehavior<TRequest, TResponse>
    : IPipelineBehavior<TRequest, TResponse>
    where TRequest : IRequest<TResponse>
{
    private readonly Stopwatch _timer;
    private readonly ILogger<PerformanceBehavior<TRequest, TResponse>> _logger;
    private const int WarningThresholdMs = 3000;

    public PerformanceBehavior(ILogger<PerformanceBehavior<TRequest, TResponse>> logger)
    {
        _timer = new Stopwatch();
        _logger = logger;
    }

    public async Task<TResponse> Handle(
        TRequest request,
        RequestHandlerDelegate<TResponse> next,
        CancellationToken cancellationToken)
    {
        _timer.Restart();

        var response = await next();

        _timer.Stop();

        var elapsedMs = _timer.ElapsedMilliseconds;

        if (elapsedMs > WarningThresholdMs)
        {
            _logger.LogWarning(
                "⚠️  Yavaş Request: [{RequestName}] -> {ElapsedMs}ms (Eşik: {Threshold}ms)",
                typeof(TRequest).Name,
                elapsedMs,
                WarningThresholdMs
            );
        }

        return response;
    }
}
