using MediatR;
using Microsoft.Extensions.Logging;
using PipelineDemo.Interfaces;

namespace PipelineDemo.Behaviors;

public class TransactionBehavior<TRequest, TResponse>
    : IPipelineBehavior<TRequest, TResponse>
    where TRequest : IRequest<TResponse>
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly ILogger<TransactionBehavior<TRequest, TResponse>> _logger;

    public TransactionBehavior(
        IUnitOfWork unitOfWork,
        ILogger<TransactionBehavior<TRequest, TResponse>> logger)
    {
        _unitOfWork = unitOfWork;
        _logger = logger;
    }

    public async Task<TResponse> Handle(
        TRequest request,
        RequestHandlerDelegate<TResponse> next,
        CancellationToken cancellationToken)
    {
        // Sadece Command'lara transaction uygula
        if (request is not ICommand<TResponse>)
            return await next();

        var requestName = typeof(TRequest).Name;

        _logger.LogInformation("🔒 Transaction Başlatıldı: [{RequestName}]", requestName);

        await using var transaction = await _unitOfWork.BeginTransactionAsync(cancellationToken);

        try
        {
            var response = await next();

            await _unitOfWork.CommitAsync(cancellationToken);
            await transaction.CommitAsync(cancellationToken);

            _logger.LogInformation("✅ Transaction Commit: [{RequestName}]", requestName);

            return response;
        }
        catch (Exception ex)
        {
            await transaction.RollbackAsync(cancellationToken);

            _logger.LogError(ex, "🔴 Transaction Rollback: [{RequestName}]", requestName);
            throw;
        }
    }
}
