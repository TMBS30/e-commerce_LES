<%@ page import="java.util.*" %>
<%@ page import="dominio.*" %>
<%@ page import="dao.*" %>


<head>
    <title>PageBot | PageStation</title>
    <link rel="stylesheet" type="text/css" href="css/chatBot.css">
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

        // Função para transformar URLs em links clicáveis
        function linkify(text) {
            const urlRegex = /(https?:\/\/[^\s]+)/g; // Detecta URLs que começam com http:// ou https://
            return text.replace(urlRegex, (url) => {
                return `<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`;
            });
        }

        function enviarMensagem() {
            const mensagemInput = document.getElementById('mensagemUsuario');
            const mensagem = mensagemInput.value.trim(); // .trim() para remover espaços em branco

            if (!mensagem) { // Não envia mensagens vazias
                return;
            }

            const messagesContainer = document.getElementById('messagesContainer');

            // Adiciona a mensagem do usuário
            messagesContainer.innerHTML += `<div class="message user-message"><p>${mensagem}</p></div>`;
            scrollToBottom(); // Rola para a nova mensagem

            fetch('chatApi', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `mensagemUsuario=${encodeURIComponent(mensagem)}`
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                let resposta = data.botMessage; // Pega a resposta do bot

                // APLICA A FUNÇÃO linkify ANTES DE INSERIR NO HTML
                resposta = linkify(resposta);

                messagesContainer.innerHTML += `<div class="message bot-message"><p>${resposta}</p></div>`;
                scrollToBottom(); // Rola para a nova mensagem
                mensagemInput.value = ''; // Limpa o campo de entrada
            })
            .catch(error => {
                console.error('Erro:', error);
                messagesContainer.innerHTML += `<div class="message bot-message error-message"><p><strong>Bot:</strong> Ocorreu um erro ao processar sua mensagem.</p></div>`;
                scrollToBottom();
                mensagemInput.value = '';
            });
        }
    </script>
</main>
</body>