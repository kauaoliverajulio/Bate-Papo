Um sistema de bate-papo multiusuário via console, desenvolvido em Java puro para demonstrar os fundamentos da programação em rede com a arquitetura Cliente-Servidor.

Índice
Sobre o Projeto
Funcionalidades
Tecnologias Utilizadas
Como Executar
Estrutura do Projeto
Melhorias Futuras
Licença
Sobre o Projeto
Este projeto implementa uma sala de chat simples onde múltiplos usuários podem se conectar a um servidor central e trocar mensagens de texto em tempo real. Toda a comunicação acontece através do terminal (console).

O principal objetivo é servir como um projeto educacional para entender na prática conceitos essenciais de redes e concorrência em Java, como:

Sockets TCP/IP
Arquitetura Cliente-Servidor
Streams de Input/Output
Multithreading para lidar com múltiplos clientes simultaneamente
Funcionalidades
✅ Chat em Tempo Real: Mensagens enviadas por um cliente são instantaneamente retransmitidas para todos os outros.
✅ Suporte a Múltiplos Clientes: O servidor é capaz de gerenciar várias conexões de clientes ao mesmo tempo.
✅ Nomes de Usuário: Cada cliente se identifica com um nome ao entrar no chat.
✅ Interface via Console: Interação simples e direta através de qualquer terminal padrão.
Tecnologias Utilizadas
Java (JDK 8 ou superior): Linguagem principal do projeto.
Java Sockets (java.net): API nativa do Java para comunicação em rede via TCP.
Java Threads: Para garantir que o servidor possa lidar com múltiplos clientes de forma concorrente.
Como Executar
Siga os passos abaixo para compilar e executar o projeto.

Pré-requisitos
Certifique-se de ter o JDK (Java Development Kit) 8 ou superior instalado e configurado no seu sistema. Você pode verificar com o comando java -version.
Passos para Execução
Clone ou Faça o Download

Faça o download dos arquivos ServidorChat.java e ClienteChat.java e coloque-os na mesma pasta.
Compile os Arquivos

Abra um terminal ou prompt de comando.
Navegue até a pasta onde você salvou os arquivos usando o comando cd.
Compile todos os arquivos .java com o seguinte comando:
<!-- end list -->

Bash

javac *.java
Isso irá gerar os arquivos ServidorChat.class e ClienteChat.class.
Inicie o Servidor 🚀

No mesmo terminal, inicie o servidor. Ele ficará aguardando conexões.
<!-- end list -->

Bash

java ServidorChat
Você verá a mensagem: Servidor de chat iniciado na porta 5000.
Inicie os Clientes 💻

Abra um novo terminal para cada cliente que você deseja conectar.
Em cada novo terminal, navegue até a mesma pasta do projeto e execute o programa cliente:
<!-- end list -->

Bash

java ClienteChat
Siga as instruções: digite um nome para o usuário e comece a enviar mensagens! O que for digitado em um cliente aparecerá no servidor e em todos os outros clientes.
Estrutura do Projeto
/projeto-chat
  ├── ServidorChat.java    // Contém a lógica do servidor central
  └── ClienteChat.java     // Contém a lógica do programa cliente
ServidorChat.java: Responsável por aceitar conexões, criar uma thread para cada cliente e retransmitir as mensagens recebidas.
ClienteChat.java: Responsável por se conectar ao servidor, enviar as mensagens do usuário e exibir as mensagens recebidas de outros.
Melhorias Futuras
Este projeto é uma base. Aqui estão algumas ideias para expandi-lo:

💡 Implementar uma Interface Gráfica (GUI) usando Swing ou JavaFX.
💡 Adicionar comandos especiais (ex: /list para ver usuários online, /quit para sair).
💡 Criar salas de chat privadas.
💡 Criptografar as mensagens para uma comunicação segura.
💡 Salvar o histórico do chat em um arquivo de log.
