# Cofre Gastronômico — Contexto do Projeto

## Dados Acadêmicos
- **Matéria:** Desenvolvimento Mobile
- **Aluno:** Gustavo Tesin (gustavo.tesin@monest.com.br)
- **Plataforma:** Android nativo
- **IDE:** Android Studio
- **Linguagem:** Java (obrigatório)
- **Build:** Groovy DSL (`build.gradle`, não `.kts`)

---

## Projeto no Android Studio
- **Nome do projeto:** MinhaCulinriaApp
- **Package:** `com.example.minhaculinriaapp`
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Localização:** `C:\Users\Gustavo\AndroidStudioProjects\MinhaCulinriaApp`

---

## Tema: Cofre Gastronômico
**Sistema Pessoal de Catalogação Gastronômica e Rastreamento de Evolução Culinária**

### Problema que resolve
Aplicativos de receitas são manuais estáticos — ignoram a curva de aprendizado prático. O Cofre Gastronômico é uma ferramenta **offline-first** que permite catalogar receitas e documentar o histórico de cada execução (tentativa e erro), registrando adaptações e avaliando resultados.

---

## Funcionalidades do App

### 1. Gestão de Acervo Relacional (CRUD)
- Entidades: **Categorias Gastronômicas**, **Receitas**, **Ingredientes**, **Passos de Preparo**
- Persistência local via **SQLite** com a biblioteca **Room**
- Chaves estrangeiras para integridade referencial entre entidades

### 2. Diário de Execuções e Versionamento de Pratos
- Registro do histórico de cada tentativa de preparo
- Campos: data, nota de autoavaliação qualitativa, observações técnicas de melhoria
- Integração com **API de Câmera nativa** para foto do prato finalizado
- Gerenciamento de arquivos de mídia localmente

### 3. Modo de Execução Hands-Free (Mão na Massa)
- Interface otimizada para o momento do cozimento
- **WakeLock** (via `WindowManager`) para manter a tela sempre ligada
- **SpeechRecognizer** em modo offline para avançar/retroceder passos por voz
- Evita contato com a tela durante o manuseio de alimentos

### 4. Temporização Concorrente em Segundo Plano
- Múltiplos cronômetros independentes vinculados a passos específicos da receita
- Implementado com **Foreground Services** + **AlarmManager**
- Emite **notificações locais** mesmo com o app minimizado

### 5. Widget na Tela Inicial
- Componente **AppWidgetProvider** no Launcher do Android
- Exibe status dos temporizadores em andamento sem abrir o app

---

## Stack Técnica

| Componente | Tecnologia |
|---|---|
| Linguagem | Java |
| IDE | Android Studio |
| Banco de dados | SQLite + Room |
| Câmera | CameraX |
| Reconhecimento de voz | SpeechRecognizer (offline) |
| Tela sempre ligada | WakeLock (WindowManager) |
| Timers em background | Foreground Service + AlarmManager |
| Notificações | NotificationManager (local) |
| Widget | AppWidgetProvider |
| Navegação | Jetpack Navigation Component |
| UI | XML Views + BottomNavigationView |

---

## Entregas do Projeto

### ✅ Entrega Parcial 1 — Proposta (entregue)
- Documento de proposta com tema, objetivo e funcionalidades

### 🔄 Entrega Parcial 2 — Desenvolvimento Inicial (prazo: 26/04/2026)
- [ ] Estrutura de pacotes organizada
- [ ] Telas iniciais (Home, Receitas, Diário, Categorias)
- [ ] Navegação básica com BottomNavigationView

### 📋 Entrega Final (a definir)
- Implementação completa de todas as funcionalidades

---

## Estrutura de Pacotes Planejada

```
com.example.minhaculinriaapp/
├── data/
│   ├── database/       # AppDatabase (Room)
│   ├── entity/         # Categoria, Receita, Ingrediente, Passo, Execucao
│   ├── dao/            # DAOs de cada entidade
│   └── repository/     # Repositórios
├── ui/
│   ├── home/           # HomeFragment
│   ├── receitas/       # ReceitasFragment + detalhes
│   ├── diario/         # DiarioFragment
│   ├── categorias/     # CategoriasFragment
│   └── execucao/       # Modo Hands-Free
├── viewmodel/          # ViewModels para cada tela
├── service/            # TimerForegroundService
├── widget/             # AppWidgetProvider
└── MainActivity.java   # Container com BottomNavigation
```

---

## Observações Importantes
- O projeto é **offline-first** — zero dependência de internet
- **NÃO usar Kotlin** — apenas Java
- **NÃO usar Jetpack Compose** — apenas XML + Views
- Integridade referencial garantida por foreign keys no Room
- Mídias (fotos) armazenadas localmente no dispositivo

---

*Contexto gerado em 26/04/2026 via Cowork*
