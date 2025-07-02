<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>

<head>
    <title>PageBot | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/chatBot.css?v=<%= System.currentTimeMillis()%>">
</head>

<body>

<header>
    <a href="servlet?action=voltarHomePage" class="logo-link">
        <h1>PageStation</h1>
        <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
    </a>
</header>

<main class="chat-container">
    <div class="menu-fixo">
        <a href="#" class="chatbot">
            <p class="texto-chatbot">ChatBot</p>
            <img class="icone-chatbot"src="Imagens/Icone - ChatBot.svg" alt="Icone ChatBot">
        </a>

        <div class="input-pesquisa_livro">
            <input class="input-pesquisa"type="text" name="pesquisa_livro" id="pesquisa_livro" placeholder="Procurar livro...">
        </div>

        <a href="#" class="link-categorias">
            <p class="categorias">Categorias</p>
        </a>

        <a href="servlet?action=exibirPedidos" class="link-pedidos">
            <p class="pedidos">Pedidos</p>
        </a>

        <a href="carrinho.jsp" class="link-carrinho">
            <img class="icone-carrinho" src="Imagens/Icone - Carrinho.svg" alt="Icone Carrinho de Compras">
        </a>

        <a href="servlet?action=consultarInfoPessoais" class="link-user">
            <img class="icone-user" src="Imagens/Icone - User.svg" alt="Icone Consultar Informacoes Cadastrais">
        </a>
    </div>

    <h2>PageBot</h2>

    <div class="chat-area">
        <div id="messagesContainer" class="messages-container">
            </div>

        <div class="input-send-container">
            <form id="chatbotForm" onsubmit="enviarMensagem(); return false;">
                <input type="text" name="mensagemUsuario" id="mensagemUsuario" placeholder="Digite sua mensagem..." required />
                <button type="submit">Enviar</button>
            </form>
        </div>
    </div>

    <div class="buttons-container">
        <button class="voltar-button" onclick="window.location.href='homePage.jsp'">Voltar</button>
    </div>

    <script>
        // Função para rolar o contêiner de mensagens para o final
        function scrollToBottom() {
            const messagesContainer = document.getElementById('messagesContainer');
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }

        // Função para transformar URLs em links clicáveis e formatar negrito
        function formatBotResponse(text) {
            // Transforma URLs em links clicáveis
            const urlRegex = /(https?:\/\/[^\s]+)/g;
            let formattedText = text.replace(urlRegex, (url) => {
                return `<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`;
            });

            // Adiciona quebras de linha para texto multi-linha (se a resposta do Gemini tiver \n)
            formattedText = formattedText.replace(/\n/g, '<br>');

            // Formata negrito para texto entre asteriscos duplos (**)
            // Isso é útil se o Gemini gerar **titulo**
            formattedText = formattedText.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

            return formattedText;
        }

        function enviarMensagem() {
            const mensagemInput = document.getElementById('mensagemUsuario');
            const mensagem = mensagemInput.value.trim();

            if (!mensagem) {
                return;
            }

            const messagesContainer = document.getElementById('messagesContainer');

            // Adiciona a mensagem do usuário
            messagesContainer.innerHTML += `<div class="message user-message"><p>${mensagem}</p></div>`;
            scrollToBottom();

            // MUDANÇA 1: Alterado o endpoint para 'chatBot'
            // MUDANÇA 2: Alterado o 'Content-Type' para 'application/json'
            // MUDANÇA 3: O corpo da requisição agora é um JSON stringificado
            fetch('chatBot', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // Alterado para JSON
                },
                body: JSON.stringify({ message: mensagem }) // Envia um JSON com a mensagem
            })
            .then(response => {
                if (!response.ok) {
                    // Se o status HTTP for 500, ainda podemos querer ler a resposta do erro
                    // para mostrar ao usuário, se o servlet retornar um JSON de erro.
                    // Mas, por enquanto, lança o erro padrão.
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // MUDANÇA 4: A resposta do bot agora está em 'data.response'
                let resposta = data.response;

                // Aplica a função de formatação antes de inserir no HTML
                resposta = formatBotResponse(resposta);

                messagesContainer.innerHTML += `<div class="message bot-message"><p>${resposta}</p></div>`;
                scrollToBottom();
                mensagemInput.value = '';
            })
            .catch(error => {
                console.error('Erro:', error);
                messagesContainer.innerHTML += `<div class="message bot-message error-message"><p><strong>Bot:</strong> Ocorreu um erro ao processar sua mensagem. Por favor, tente novamente mais tarde.</p></div>`;
                scrollToBottom();
                mensagemInput.value = '';
            });
        }
    </script>
</main>
</body>