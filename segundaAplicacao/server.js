const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();

//Recursos
let estadosDoRecurso = { released: "released", held: "held", wanted: "wanted" };
let recurso = estadosDoRecurso.released;
let filaParaUsarORecurso = [];
let listaDePeers = [];

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

//endpoint que mostra os eventos
app.get('/recursos', receberRequisicoes);

// Essa é a parte principal
function receberRequisicoes(requisicao, resposta, proximo) {
    const headers = {
        'Content-Type': 'text/event-stream',
        'Connection': 'keep-alive',
        'Cache-Control': 'no-cache'
    };
    //Só os cabeçalhos
    resposta.writeHead(200, headers);

    const dado = `dados: ${JSON.stringify(recurso)}\n\n`;
    if (requisicao.query.token) {
        if (recurso == estadosDoRecurso.released) {
            recurso = estadosDoRecurso.held;
        }
        if (recurso == estadosDoRecurso.held) {
            filaParaUsarORecurso.push({ token: requisicao.query.token });
        }
    }

    //Aqui é a resposta:
    resposta.write(dado);

    const peerId = Date.now();

    const novopeer = {
        id: peerId,
        resposta
    };

    //metodo que a cada segundo checa de o recurso está livre se estiver avisa em broadcast
    setTimeout(function checaSeRecursoLivreEAvisa() {
        if (filaParaUsarORecurso.length > 0) {

        }
    }, 1000);


    //Remove o peer quando a conexão é fechada
    requisicao.on('close', () => {
        console.log(`${peerId} Conexão fechada`);
        filaParaUsarORecurso = filaParaUsarORecurso.filter(client => client.id !== peerId);
    });
}

let token = Math.random();



//Outra partes apenas para mostrar informção
app.get('/', (requisicao, resposta) => {
    const headers = {
        'Content-Type': 'text/html'
    };
    resposta.writeHead(200, headers);

    const instrucoes = `<p>Ouvindo em http://localhost:${PORTA}</p>
  <p>Mostrados numero de peers conectados em <a href="http://localhost:${PORTA}/peersConectados">http://localhost:${PORTA}/peersConectados</p>
  <p>Mostrados dados enviados em <a href="http://localhost:${PORTA}/recursos">http://localhost:${PORTA}/recursos</p>
  <p>Para enviar use o index.html ou faca um get em <a href="http://localhost:${PORTA}/acessarRecurso?token=meuId">http://localhost:${PORTA}/acessarRecurso?token=meuId</p>
  `

    resposta.write(instrucoes);
})

app.get('/peersConectados', (requisicao, resposta) => resposta.json({ peers: filaParaUsarORecurso.length }));

const PORTA = 3000;

app.listen(PORTA, () => {
    console.log(`Ouvindo em http://localhost:${PORTA}
  Mostrados numero de peers conectados em http://localhost:${PORTA}/peersConectados
  Mostrados dados enviados em http://localhost:${PORTA}/recursos
  Para enviar use o index.html ou faça um get em http://localhost:${PORTA}/acessarRecurso?token=meuId
  `);
});

function mandarEventosParaTodos(novoFato) {
    listaDePeers.forEach(peer => peer.resposta.write(`dados: ${JSON.stringify(novoFato)}\n\n`));
}

async function adicionarDados(requisicao, resposta, proximo) {
    const token = requisicao.query.token;
    listaDePeers.push(token);
    resposta.json(token);
    return mandarEventosParaTodos(token);
}

app.get('/acessarRecurso', adicionarDados);