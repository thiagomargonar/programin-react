# programin-react
Curso de programação reativa com exemplos e comentários em português.

<h2>O que é programação reativa?</h2>
<p>Quando falamos em programação reativa,
estamos falando basicamente de um conceito que através dele podemos 
ter comportamentos no nosso sistema capaz de lidar com eventos de forma
assíncrona.
Este conceito estende as características do pattern Observer
e tem como ponto focal 4 pilares escritos no "manifesto reativo", 
sendo eles: </p>
    <ul>
        <li><h3><b>Responsivos</b></h3></li>
        <p>Se possível responde em tempo de execução, 
            porem, se o processo for algo demorado como a geração de um 
            relatório, por exemplo, ele não aguarda a execução para iniciar uma próxima interação.</p> 
    </ul>
    <ul>
        <li><h3><b>Resiliência</b></h3></li>
        <p>Se detectado erros, por exemplo, com resiliência eles são tratados de forma responsiva,
        sendo assim, o sistema é capaz de se recuperar sem comprometer todo o sistema, isolando o problema e
        tratando o erro de forma única.</p> 
    </ul>
    <ul>
        <li><h3><b>Elástico(escalável)</b></h3></li>
        <p>A elasticidade tem por usa vez a responsabilidade de escalar a aplicação quando existe variações 
        de carga, sendo capaz de se auto organizar conforme sua necessidade, fazendo assim que não
        haja nenhum "funil" para a aplicação ter "gargalos".</p> 
    </ul>
    <ul>
        <li><h3><b>Guiado por mensagem</b></h3></li>
        <p>Aqui os componentes são desacoplados e colaboram através da troca de mensagens. Estas mensagens 
        geralmente são trafegadas entre um meio transparente entre os componentes, como um barramento
        de eventos, Isso quer dizer que, em uma aplicação reativa, ao invés de você ter chamadas 
        explícitas entre componentes (um controller e um repository, por exemplo), você tem, na verdade
        mensagens trocadas de maneira assíncrona</p> 
    </ul>

<h3>P.s.:</h3>
<p>
    Cold -> A cada evento criado(Publish), nada acontece até que alguem inicie uma observação(Subscribe); <br/>
    Hot ->  É feito a publicação de evento criado(Publish) mesmo que ninguém esteja observando os mesmos,
    ou seja, a cada publicação de evento se ninguém estiver observando a publicação é perdida.
    Pensa na seguinte analogia: se você tirar o mouse do computador e clicar várias vezes no botão, e 
    após colocar o mouse no computador, os cliques não fica armazenado, eles serão perdido e o computador
    só estará "Observando" após a conexão ser realizada.
</p>
<h2>Programação:</h2>
<p>
    <b>Vamos ver então:</b><br/>
    Publisher. Subscriber. Subscription e Processor.<br/>
    Apis compostas por FLUX[N] e Mono[0,1].

</p>
