const {createStore} = Redux;
const {Provider, useSelector} = ReactRedux;

/* Store */

const initialState = {
     data: undefined,
     status: "idle",
     errorMessage: undefined,
 };

const gameState = (state = initialState, action) => {
    console.log({state, action});

    switch(action.type) {
        case 'update':
            console.log({payload: action.payload});
            return {
                status: "idle",
                data: action.payload,
                errorMessage: undefined
            };
        case 'clear':
            return initialState;
        case 'loading':
            return {
                ...state,
                status: 'loading',
                errorMessage: undefined,
             };
        case 'error':
            return {
                ...state,
                status: 'idle',
                errorMessage: action.payload,
             };
        default:
            return state;
    }
}

const store = createStore(gameState);
const selectGameStateExists = (state) => !!state.data;
const selectGameActive = (state) => state.data.gameStatus === "Active";
const selectWinner = (state) => state.data && state.data.winningPlayer;
const selectBoard = (state) => state.data.board;
const selectPlayers = (state) => state.data.players;
const selectPlayerTag = (playerId) => {
    return (state) => {
        const [playerOne, playerTwo] = state.data.players;
        return playerId === playerOne ? "player1" : "player2";
    }
}
const selectIsActivePlayer = (playerId) => {
    return (state) => {
        return selectGameActive(state) &&
            (playerId === selectCurrentPlayer(state));
    }
}
const selectCurrentPlayer = (state) => state.data.currentPlayer;
const selectIsLoading = (state) => state.status === 'loading';
const selectErrorMessage = (state) => state.errorMessage;
const selectSowRequestData = (state) => ({
    gameId: state.data.gameId,
    gameStateToken: state.data.gameStateToken,
    playerId: selectCurrentPlayer(state),
});

/* Actions */

const startGame = async () => {
    store.dispatch({type: 'loading'});
    const request = { method: "PUT", headers: { 'Content-Type': 'application/json' }, };
    await fetch("/game", request)
        .then(async res => {
            if(res.ok) {
                return res.json();
            } else {
                throw await res.text();
            }
        })
        .then(res => store.dispatch({ type: 'update', payload: res}))
        .catch(err => store.dispatch({ type: 'error', payload: err}));
};

const sow = async (requestData) => {
    store.dispatch({type: 'loading'});
    const request = { method: "POST", headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(requestData) };
    await fetch("/game", request)
        .then(async res => {
            if(res.ok) {
                return res.json();
            } else {
                throw await res.text();
            }
        })
        .then(res => store.dispatch({ type: 'update', payload: res}))
        .catch(err => store.dispatch({ type: 'error', payload: err}));
};


/* Components */

const StartGameButton = () => {
    return <div className="startButtonContainer">
        <button onClick={startGame} >Start a new game of Mancalamari!</button>
    </div>
}

const Game = () => {
    const [playerOne, playerTwo] = useSelector(selectPlayers);
    const errorMessage = useSelector(selectErrorMessage);
    const winner = useSelector(selectWinner);

    return <div className="game">
        <PlayerMarker playerId={playerOne} />
        <div>
            <Board />
            <div class="errorMessage">{ errorMessage }</div>
            { winner ? <div class="winner">{winner} has won the game!</div> : undefined }
        </div>
        <PlayerMarker playerId={playerTwo} />
    </div>
}

const PlayerMarker = ({playerId}: props) => {
    const playerTag = useSelector(selectPlayerTag(playerId));
    const activePlayer = useSelector(selectCurrentPlayer);
    const isPlayerActive = activePlayer === playerId;
    const className = `${playerTag} playerMarker`

    return <div className={className}>
        <div>{playerId}</div>
        <img className="avatar" src={`${playerTag}.jpg`} alt={`${playerTag} avatar`} />
        {isPlayerActive ? <div>{"It's your turn!"}</div> : undefined}
    </div>
}

const Board = () => {
    const board = useSelector(selectBoard);
    let pitId = 0;

    const {mancala, pits} = board.pits.reduce((elements, pit) => {
        const props = {...pit, pitId};
        pitId++;

        if(pit.pitType == "Pit") {
            elements.pits.push(<Pit key={pitId} {...props} />);
        } else {
            elements.mancala.push(<Mancala key={pitId} {...props} />)
        }
        return elements;
    }, { mancala: [], pits: []})

    const [playerOneMancala, playerTwoMancala] = mancala;

    const antiClockwisePits = pits.slice(6).concat(pits.slice(0,6).reverse());

    return <div className="board">
        { playerTwoMancala }
        { <div className="pits">{antiClockwisePits}</div> }
        { playerOneMancala }
    </div>
}

const Mancala = ({ seeds, ownerId, pitId }: props) => {
    const playerTag = useSelector(selectPlayerTag(ownerId));
    const className = `mancala ${playerTag}`;
    return <div id={`pitId-${pitId}`} className={className}>{seeds}</div>;
}

const Pit = ({ seeds, ownerId, pitId }: props) => {
    const playerTag = useSelector(selectPlayerTag(ownerId));
    const baseRequestData = useSelector(selectSowRequestData);
    const requestData = { ...baseRequestData, pitId };
    const active = useSelector(selectIsActivePlayer(ownerId));
    const onClick = active ? () => sow(requestData) : () => {};
    const className = `pit ${active ? playerTag + " pointer" : ""}`;

    return (
        <div id={`pitId-${pitId}`} className={className} onClick={onClick}>
            {seeds}
        </div>
    );
}

/* App */

const App = () => {
    const gameStateExists = useSelector(selectGameStateExists);

    return <div>
        { gameStateExists ? <Game /> : <StartGameButton /> }
    </div>
};


/* Render */

ReactDOM.render(<Provider store={store}><App /></Provider>, document.querySelector('#App'));