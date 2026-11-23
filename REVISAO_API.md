# Revisão de integração OROSCORP
Base: ZIP mobile, ZIP servidor, SQL e quatro imagens do protótipo enviados. Revisão estática; não foi compilado nem executado contra MySQL.

## Comparação de telas
- Login: design existente preservado; resposta real `response.dados` e token; acesso ADM exigido.
- Recuperação/código/nova senha: três POST reais, navegação após sucesso, retorno e mensagens de erro. Requer tabela de recuperação e SMTP.
- Dashboard/menu: nome do usuário, contagem de não identificados, câmeras e gráfico por primeira evidência datada. Gráfico em barras em vez da curva do protótipo; índice de segurança e total de hoje indisponíveis (não há data de criação da ocorrência nem fórmula).
- Perfil: foto, nome, email e nascimento reais. CNPJ não fornecido no login.
- Funcionários: tabela e paginação existentes; normalização de cargo/setor/status aninhados.
- Setores/detalhes: preservados; IDs normalizados, consultas de evidências para datas quando disponíveis. Sem valores de segurança fictícios.
- Câmeras: nomes dos setores nos filtros e cartões, status e endereço; módulo RTSP acrescentado. Ainda não há miniaturas ao vivo, contagem de pessoas ou análise de vídeo na lista.
- Câmera individual: player existente, dependência RTSP; requer stream real acessível.
- Ocorrências: origem, prioridade, funcionário, motivo/status/setor reais; filtros existentes. Resolvida considerada fechada.
- Detalhe manual/IA: descrição, EPI, primeira evidência, horário e confiança reais; não há histórico de transições na API. Não implementadas exportação PDF, identificação nem alterações de status devido a PUT destrutivo de vínculos e ausência de contratos específicos seguros.
- Registro: POST conforme controller (id_epi como objetos, cameras como array). Apenas sem evidência; URI local não é URL acessível ao servidor. Data/hora não persistem sem evidência. Prioridades conforme SQL (sem Crítica).

## Configuração
Abra no Android Studio, sincronize Gradle. minSdk 27 preservado. ApiConfig.kt: 10.0.2.2 para emulador, IP do servidor para celular físico. Rede HTTP apenas em desenvolvimento. Inicie servidor/MySQL com configuração local e credenciais válidas. Execute testes de login, recuperação, listas, filtros, stream e cadastro sem evidência. Não há SDK Android ou servidor em execução neste ambiente.

## Pendências no servidor/SQL
O SQL separado contém id_categoria nos inserts mas não na tabela EPI, origem IA/manual invertida, bcrypt de exemplo inválido e reset sem tbl_adm. O DAO de recuperação exige tbl_recuperacao_senha (não encontrada nos scripts CREATE enviados) e SMTP. O PUT de ocorrências recria relações e pode apagar evidências; precisa de transação e rotas específicas. O POST também não é transacional. Não enviar senha/hash em consultas de usuário. Não usar o reset em produção. A autenticação e autorização precisam ser verificadas em todas as rotas sensíveis no servidor.
