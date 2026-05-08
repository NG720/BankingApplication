# PipelineDemo — MediatR Pipeline Behaviors

MediatR pipeline behavior örneği. Üç cross-cutting concern uygulanmıştır.

## Klasör Yapısı

```
PipelineDemo/
├── Behaviors/
│   ├── LoggingBehavior.cs        # Request/Response loglama
│   ├── PerformanceBehavior.cs    # 3000ms+ yavaş request uyarısı
│   └── TransactionBehavior.cs    # Command'lar için DB transaction
├── Interfaces/
│   ├── ICommand.cs               # Command marker interface
│   └── IUnitOfWork.cs            # Transaction soyutlaması
├── ServiceCollectionExtensions.cs # DI kaydı
└── README.md
```

## Pipeline Akışı

```
Request
  │
  ▼  LoggingBehavior      → 📥 Request loglanır
  │
  ▼  PerformanceBehavior  → ⏱️  Stopwatch başlar
  │
  ▼  TransactionBehavior  → 🔒 Transaction açılır (sadece ICommand ise)
  │
  ▼  Handler              → İş mantığı çalışır
  │
  ▼  TransactionBehavior  → ✅ Commit / 🔴 Rollback
  │
  ▼  PerformanceBehavior  → ⚠️  3000ms+ ise uyarı
  │
  ▼  LoggingBehavior      → 📤 Response loglanır
```

## Kullanım

```csharp
// Program.cs
builder.Services.AddPipelineBehaviors();
```
