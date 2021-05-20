const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.get('/', (requisicao, resposta) => {
    const headers = {
        'Content-Type': 'text/html'
    };
    resposta.writeHead(200, headers);

    const instrucoes = `<p>Ouvindo em http://localhost:${PORTA}</p>
  <p>Mostrados numero de peers conectados em <a href="http://localhost:${PORTA}/peersConectados">http://localhost:${PORTA}/peersConectados</p>
  <p>Mostrados dados enviados em <a href="http://localhost:${PORTA}/dadosEnviados">http://localhost:${PORTA}/dadosEnviados</p>
  <p>Para enviar use o index.html ou faca um post em <a href="http://localhost:${PORTA}/enviarDados">http://localhost:${PORTA}/enviarDados</p>
  `

    resposta.write(instrucoes);
})

app.get('/peersConectados', (requisicao, resposta) => resposta.json({ peers: filaParaUsarORecurso.length }));

const PORTA = 3000;

let filaParaUsarORecurso = [];
let dadosEnviados = [];

app.listen(PORTA, () => {
    console.log(`Ouvindo em http://localhost:${PORTA}
  Mostrados numero de peers conectados em http://localhost:${PORTA}/peersConectados
  Mostrados dados enviados em http://localhost:${PORTA}/dadosEnviados
  Para enviar use o index.html ou faça um post em http://localhost:${PORTA}/enviarDados
  `);
});

// 
function receberRequisicoes(requisicao, resposta, proximo) {
    const headers = {
        'Content-Type': 'text/event-stream',
        'Connection': 'keep-alive',
        'Cache-Control': 'no-cache'
    };
    resposta.writeHead(200, headers);

    const dado = `dados: ${JSON.stringify(dadosEnviados)}\n\n`;

    resposta.write(dado);

    const peerId = Date.now();

    const novopeer = {
        id: peerId,
        resposta
    };

    //Recebendo token
    if (requisicao.body.token != '') {
        //Cria um novo peer quando a conexão é aberta e o token está certo
        filaParaUsarORecurso.push(novopeer);

    }


    //Remove o peer quando a conexão é fechada
    requisicao.on('close', () => {
        console.log(`${peerId} Conexão fechada`);
        filaParaUsarORecurso = filaParaUsarORecurso.filter(client => client.id !== peerId);
    });
}

let token = Math.random();

//endpoint que mostra os eventos
app.get('/dadosEnviados', receberRequisicoes);

function mandarEventosParaTodos(novoFato) {
    filaParaUsarORecurso.forEach(peer => peer.resposta.write(`dados: ${JSON.stringify(novoFato)}\n\n`));
}

async function adicionarDados(requisicao, resposta, proximo) {
    const novoFato = requisicao.body;
    dadosEnviados.push(novoFato);
    resposta.json(novoFato);
    return mandarEventosParaTodos(novoFato);
}

app.post('/enviarDados', adicionarDados);