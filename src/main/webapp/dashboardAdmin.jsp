<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.List, java.util.Map"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Admin | PageStation</title>
    <!-- Tailwind CSS CDN (mantido para estilos de componentes) -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    // Mantém a fonte Inter para Tailwind, mas o CSS customizado usará Montserrat
                    // Se quiser que Tailwind também use Montserrat para classes padrão, adicione aqui:
                    // fontFamily: {
                    //     sans: ['Montserrat Alternates', 'sans-serif'],
                    // },
                },
            },
        };
    </script>
    <!-- Font Awesome CDN para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <!-- Chart.js CDN para gráficos -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- SEU NOVO ARQUIVO DE ESTILOS CSS (com o nome atualizado e timestamp) -->
    <link rel="stylesheet" href="css/dashboard.css?v=<%= System.currentTimeMillis()%>">
</head>
<body>
    <!-- Cabeçalho -->
    <header>
        <a href="servlet?action=voltarHomePageADM" class="logo-link">
            <h1>PageStation</h1>
            <img src="Imagens/PageStation - Logo.png" alt="Logo PageStation">
        </a>
    </header>

    <!-- Menu Fixo -->
    <div class="menu-fixo">
        <div class="texto-modo-adm">
             <p class="texto-adm">Modo: ADM</p>
        </div>
        <a href="servlet?action=exibirEstoque" class="link-estoque">
            <p class="estoque">Estoque</p>
        </a>
        <a href="servlet?action=exibirPedidosADM" class="link-pedidos">
            <p class="pedidos">Pedidos</p>
        </a>
        <a href="servlet?action=exibirDashboardADM" class="link-dashboard">
            <p class="dashboard">Dashboard</p>
        </a>
    </div>

    <!-- Conteúdo Principal do Dashboard -->
    <div class="main-content-wrapper">
        <!-- Tela de Carregamento -->
        <div id="loading-screen" class="flex flex-col items-center justify-center bg-white p-8 rounded-lg shadow-xl animate-pulse hidden">
            <i class="fas fa-chart-line text-blue-500 text-6xl mb-4"></i>
            <p class="text-xl font-semibold text-gray-700">Carregando Dashboard...</p>
        </div>

        <!-- Tela de Acesso Negado (ainda existe no HTML, mas nunca será mostrada pelo JS) -->
        <div id="access-denied-screen" class="flex flex-col items-center justify-center bg-gradient-to-br from-red-400 to-red-600 p-4 rounded-xl shadow-xl text-center border-4 border-red-300 hidden">
            <i class="fas fa-shield-alt text-red-500 text-6xl mb-6 animate-bounce"></i>
            <h1 class="text-3xl md:text-4xl font-bold text-red-700 mb-4">Acesso Negado</h1>
            <p class="text-lg md:text-xl text-gray-800">Você não tem permissão para visualizar este painel.</p>
            <p class="text-md text-gray-600 mt-2">Por favor, faça login com uma conta de administrador.</p>
        </div>

        <!-- Dashboard Principal -->
        <div id="dashboard-content" class="max-w-7xl w-full bg-white rounded-2xl shadow-xl p-6 sm:p-10 border border-gray-200 hidden">
            <header class="mb-8 text-center dash_header">
                <h1 class="text-3xl sm:text-4xl font-extrabold text-gray-900 mb-2">
                    <i class="fas fa-chart-line text-blue-600 mr-3 align-middle"></i>
                    Dashboard Administrativo de Vendas
                </h1>
                <p class="text-lg text-gray-600">Visão geral das principais métricas de vendas ao longo do tempo.</p>
            </header>

            <!-- Seção de Filtro de Período -->
            <section class="bg-gray-100 p-6 rounded-xl shadow-md border border-gray-200 mb-8 flex flex-col sm:flex-row items-center justify-center space-y-4 sm:space-y-0 sm:space-x-4">
                <div class="flex flex-col sm:flex-row items-center space-y-2 sm:space-y-0 sm:space-x-4">
                    <label for="startDate" class="font-medium text-gray-700">De:</label>
                    <input type="date" id="startDate" class="p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500">
                    <label for="endDate" class="font-medium text-gray-700">Até:</label>
                    <input type="date" id="endDate" class="p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500">
                </div>
                <button id="filterButton" class="px-6 py-2 bg-blue-600 text-white font-semibold rounded-md shadow-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75 transition-all duration-200">
                    <i class="fas fa-filter mr-2"></i>Filtrar
                </button>
            </section>

            <section class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <!-- Card: Vendas Totais -->
                <div class="bg-blue-50 p-6 rounded-xl shadow-md border border-blue-200 flex items-center space-x-4">
                    <div class="p-3 bg-blue-200 rounded-full">
                        <i class="fas fa-dollar-sign text-blue-700 text-2xl"></i>
                    </div>
                    <div>
                        <p class="text-sm font-medium text-gray-600">Vendas Totais (Período)</p>
                        <h2 id="total-sales" class="text-2xl font-bold text-blue-800"></h2>
                    </div>
                </div>
                <!-- Card: Total de Pedidos -->
                <div class="bg-green-50 p-6 rounded-xl shadow-md border border-green-200 flex items-center space-x-4">
                    <div class="p-3 bg-green-200 rounded-full">
                        <i class="fas fa-shopping-bag text-green-700 text-2xl"></i>
                    </div>
                    <div>
                        <p class="text-sm font-medium text-gray-600">Total de Pedidos (Período)</p>
                        <h2 id="total-orders" class="text-2xl font-bold text-green-800"></h2>
                    </div>
                </div>
                <!-- Card: Valor Médio do Pedido -->
                <div class="bg-yellow-50 p-6 rounded-xl shadow-md border border-yellow-200 flex items-center space-x-4">
                    <div class="p-3 bg-yellow-200 rounded-full">
                        <i class="fas fa-sack-dollar text-yellow-700 text-2xl"></i>
                    </div>
                    <div>
                        <p class="text-sm font-medium text-gray-600">Valor Médio do Pedido</p>
                        <h2 id="avg-order-value" class="text-2xl font-bold text-yellow-800"></h2>
                    </div>
                </div>
            </section>

            <div id="chart-and-categories-container">
                 <div class="w-full h-96 bg-gray-50 rounded-xl shadow-inner border border-gray-200 p-4 mb-8">
                     <h3 class="text-xl font-semibold text-gray-800 mb-4 text-center">Tendências de Vendas Diárias</h3>
                     <canvas id="salesChart" class="w-full h-full"></canvas>
                 </div>

                 <section class="bg-purple-50 p-6 rounded-xl shadow-md border border-purple-200">
                     <h3 class="text-xl font-semibold text-gray-800 mb-4 text-center">
                         <i class="fas fa-tags text-purple-700 mr-2"></i>Top 3 Categorias Mais Vendidas (Receita)
                     </h3>
                     <ul id="top-categories-list" class="list-none p-0 space-y-2">
                         <!-- As categorias serão injetadas aqui pelo JS -->
                     </ul>
                 </section>
            </div>
        </div>
    </div>

    <script>
        let salesChartInstance = null; // Para armazenar a instância do Chart.js

        // Função para formatar valores monetários
        const formatCurrency = (value) => {
            return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
        };

        // --- FUNÇÃO checkAdminStatus SIMPLIFICADA PARA SEMPRE PERMITIR ACESSO ---
        const checkAdminStatus = async () => {
            // Conforme solicitado, para fins de projeto,
            // assumimos que se o usuário chegou a esta JSP,
            // ele já foi autenticado como administrador no servidor.
            // Retorna true para sempre exibir o dashboard.
            return true;
        };

        // Função para buscar dados reais do backend (chamada via AJAX)
        // Agora aceita startDate e endDate como parâmetros
        const fetchDashboardData = async (startDate, endDate) => {
            let url = 'api/dashboard-data';
            const params = new URLSearchParams();

            if (startDate) {
                params.append('startDate', startDate);
            }
            if (endDate) {
                params.append('endDate', endDate);
            }

            if (params.toString()) {
                url += '?' + params.toString();
            }

            try {
                const response = await fetch(url);
                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`Erro HTTP ao buscar dados: ${response.status} - ${errorText}`);
                }
                const data = await response.json();
                return {
                    salesData: data.dailySales || [],
                    categoryData: data.topCategories || []
                };
            } catch (error) {
                console.error("Erro ao buscar dados do dashboard:", error);
                return { salesData: [], categoryData: [] };
            }
        };

        // Função para exibir/ocultar as diferentes telas do dashboard
        const showScreen = (screenId) => {
            document.getElementById('loading-screen').classList.add('hidden');
            document.getElementById('access-denied-screen').classList.add('hidden');
            document.getElementById('dashboard-content').classList.add('hidden');
            document.getElementById(screenId).classList.remove('hidden');
        };

        // Função para renderizar os cartões de métricas sumárias
        const renderSummaryCards = (data) => {
            const totalSales = data.reduce((sum, d) => sum + d.totalSales, 0);
            const totalOrders = data.reduce((sum, d) => sum + d.orders, 0);
            const avgOverallOrderValue = totalOrders > 0 ? totalSales / totalOrders : 0;

            document.getElementById('total-sales').innerText = formatCurrency(totalSales);
            document.getElementById('total-orders').innerText = totalOrders.toLocaleString('pt-BR');
            document.getElementById('avg-order-value').innerText = formatCurrency(avgOverallOrderValue);
        };

        // Função para configurar e renderizar o gráfico de linhas de vendas
        const setupSalesChart = (data) => {
            const ctx = document.getElementById('salesChart').getContext('2d');

            if (salesChartInstance) {
                salesChartInstance.destroy();
            }

            salesChartInstance = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: data.map(d => d.date),
                    datasets: [
                        {
                            label: 'Vendas Totais (R$)',
                            data: data.map(d => d.totalSales),
                            borderColor: 'rgb(136, 132, 216)',
                            backgroundColor: 'rgba(136, 132, 216, 0.2)',
                            tension: 0.3,
                            yAxisID: 'y1',
                            pointBackgroundColor: 'rgb(136, 132, 216)',
                            pointBorderColor: '#fff',
                            pointHoverBackgroundColor: '#fff',
                            pointHoverBorderColor: 'rgb(136, 132, 216)',
                            borderWidth: 2,
                            pointRadius: 5,
                            pointHoverRadius: 8
                        },
                        {
                            label: 'Número de Pedidos',
                            data: data.map(d => d.orders),
                            borderColor: 'rgb(130, 202, 157)',
                            backgroundColor: 'rgba(130, 202, 157, 0.2)',
                            tension: 0.3,
                            yAxisID: 'y2',
                            pointBackgroundColor: 'rgb(130, 202, 157)',
                            pointBorderColor: '#fff',
                            pointHoverBackgroundColor: '#fff',
                            pointHoverBorderColor: 'rgb(130, 202, 157)',
                            borderWidth: 2,
                            pointRadius: 5,
                            pointHoverRadius: 8
                        },
                        {
                            label: 'Valor Médio do Pedido (R$)',
                            data: data.map(d => d.avgOrderValue),
                            borderColor: 'rgb(255, 198, 88)',
                            backgroundColor: 'rgba(255, 198, 88, 0.2)',
                            tension: 0.3,
                            yAxisID: 'y3',
                            pointBackgroundColor: 'rgb(255, 198, 88)',
                            pointBorderColor: '#fff',
                            pointHoverBackgroundColor: '#fff',
                            pointHoverBorderColor: 'rgb(255, 198, 88)',
                            borderWidth: 2,
                            pointRadius: 5,
                            pointHoverRadius: 8
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    interaction: {
                        mode: 'index',
                        intersect: false,
                    },
                    stacked: false,
                    plugins: {
                        title: {
                            display: false,
                            text: 'Tendências de Vendas Diárias'
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    if (context.dataset.label.includes('R$')) {
                                        label += formatCurrency(context.parsed.y);
                                    } else {
                                        label += context.parsed.y.toLocaleString('pt-BR');
                                    }
                                    return label;
                                }
                            }
                        }
                    },
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: 'Data',
                                color: '#666'
                            },
                            grid: {
                                display: false
                            },
                            ticks: {
                                color: '#666'
                            }
                        },
                        y1: {
                            type: 'linear',
                            display: true,
                            position: 'left',
                            title: {
                                display: true,
                                text: 'Vendas Totais (R$)',
                                color: 'rgb(136, 132, 216)'
                            },
                            grid: {
                                color: 'rgba(224, 224, 224, 0.5)'
                            },
                            ticks: {
                                color: 'rgb(136, 132, 216)',
                                callback: function(value) {
                                    return formatCurrency(value);
                                }
                            }
                        },
                        y2: {
                            type: 'linear',
                            display: true,
                            position: 'right',
                            title: {
                                display: true,
                                text: 'Número de Pedidos',
                                color: 'rgb(130, 202, 157)'
                            },
                            grid: {
                                drawOnChartArea: false,
                            },
                            ticks: {
                                color: 'rgb(130, 202, 157)',
                                callback: function(value) {
                                    return value.toLocaleString('pt-BR');
                                }
                            }
                        },
                        y3: {
                            type: 'linear',
                            display: false,
                            position: 'right',
                            grid: {
                                drawOnChartArea: false,
                            },
                            ticks: {
                                callback: function(value) {
                                    return formatCurrency(value);
                                }
                            }
                        }
                    }
                }
            });
        };

        // Função para renderizar as top categorias
        const renderTopCategories = (data) => {
            const topCategoriesList = document.getElementById('top-categories-list');
            topCategoriesList.innerHTML = '';

            const sortedCategories = [...data].sort((a, b) => b.totalRevenue - a.totalRevenue).slice(0, 3);

            if (sortedCategories.length === 0) {
                topCategoriesList.innerHTML = '<li class="text-gray-600 text-center">Nenhuma categoria de vendas encontrada.</li>';
                return;
            }

            sortedCategories.forEach((cat, index) => {
                const listItem = document.createElement('li');
                listItem.className = `flex items-center justify-between p-3 rounded-lg shadow-sm ${index % 2 === 0 ? 'bg-purple-100' : 'bg-purple-50'}`;
                listItem.innerHTML = `
                    <span class="text-lg font-medium text-purple-800"><i class="fas fa-certificate text-purple-600 mr-2"></i>${index + 1}. ${cat.category}</span>
                    <span class="text-lg font-bold text-purple-900">${formatCurrency(cat.totalRevenue)}</span>
                `;
                topCategoriesList.appendChild(listItem);
            });
        };

        // Função para carregar e exibir os dados do dashboard
        const loadDashboardData = async (startDate = null, endDate = null) => {
            showScreen('loading-screen'); // Mostra a tela de carregamento
            const { salesData, categoryData } = await fetchDashboardData(startDate, endDate);

            // Sempre mostra o conteúdo principal do dashboard, mesmo que não haja dados
            showScreen('dashboard-content');

            renderSummaryCards(salesData); // Atualiza os cartões com base nos dados (serão zeros se não houver dados)
            setupSalesChart(salesData);    // Configura e exibe o gráfico (Chart.js lida com dados vazios sem linhas)
            renderTopCategories(categoryData); // Preenche a lista de top categorias (exibirá "nenhuma" se vazio)

            // Adiciona uma mensagem de "nenhum dado" se ambos os conjuntos de dados estiverem vazios
            const noDataMessageElement = document.getElementById('no-data-message');
            const chartAndCategoriesContainer = document.getElementById('chart-and-categories-container');

            if (salesData.length === 0 && categoryData.length === 0) {
                if (!noDataMessageElement) { // Cria a mensagem se não existir
                    const messageDiv = document.createElement('div');
                    messageDiv.id = 'no-data-message';
                    messageDiv.className = 'text-center p-8 text-gray-700 mt-8';
                    messageDiv.innerHTML = `
                        <h2 class="text-2xl font-bold mb-4">Nenhum dado de vendas disponível para o período selecionado.</h2>
                        <p>Ajuste o filtro de data ou verifique o banco de dados.</p>
                    `;
                    chartAndCategoriesContainer.appendChild(messageDiv);
                } else { // Se já existir, apenas garante que esteja visível
                    noDataMessageElement.classList.remove('hidden');
                }
                // Oculta os gráficos e a seção de categorias se não houver dados
                document.querySelector('#chart-and-categories-container > div:first-child').classList.add('hidden'); // Oculta o div do canvas
                document.querySelector('#chart-and-categories-container > section').classList.add('hidden'); // Oculta a seção de categorias
            } else {
                if (noDataMessageElement) { // Oculta a mensagem se houver dados
                    noDataMessageElement.classList.add('hidden');
                }
                // Garante que os gráficos e a seção de categorias estejam visíveis se houver dados
                document.querySelector('#chart-and-categories-container > div:first-child').classList.remove('hidden');
                document.querySelector('#chart-and-categories-container > section').classList.remove('hidden');
            }
        };

        // Inicialização do Dashboard
        window.onload = async () => {
            const isAdmin = await checkAdminStatus();

            if (isAdmin) {
                // Carrega os dados iniciais sem filtro de data
                loadDashboardData();

                // Adiciona o listener para o botão de filtro
                document.getElementById('filterButton').addEventListener('click', () => {
                    const startDate = document.getElementById('startDate').value;
                    const endDate = document.getElementById('endDate').value;
                    loadDashboardData(startDate, endDate);
                });
            } else {
                showScreen('access-denied-screen');
            }
        };
    </script>
</body>
</html>
