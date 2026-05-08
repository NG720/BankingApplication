using MediatR;

namespace PipelineDemo.Interfaces;

// Command'ları Query'lerden ayırt etmek için marker interface
public interface ICommand<TResponse> : IRequest<TResponse> { }
