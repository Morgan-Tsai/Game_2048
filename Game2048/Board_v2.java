import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Board_v2 {
    private int size; // 遊戲板的大小
    private int score; // 遊戲分數
    private int emptyTiles; // 值為零的磁磚數量
    private int initTiles = 2; // 初始磁磚數量（通常為兩塊）
    private boolean gameover = false; // 當找到2048磁磚時遊戲結束
    private String wonOrLost; // 勝利或失敗
    private boolean genNewTile = false; // g當任何磁磚移動時生成新瓷磚
    private List<List<Tile>> tiles; // 遊戲板
    private int highScore = 0;

    //20240722 add previousState , previousScore
    private List<List<Tile>> previousState;
    private int previousScore;
    private Stack<GameState> previousStates = new Stack<>();
    private static final int MAX_UNDO_STEPS = 3;
    
    String direction;


    // 構造函數
    public Board_v2(int size) {
        super();
        this.size = size;
        this.emptyTiles = this.size * this.size;
        this.tiles = new ArrayList<>();

        start();
    }

    // 初始化遊戲板
    private void initialize() {
        for (int row = 0; row < this.size; row++) {
            tiles.add(new ArrayList<Tile>());
            for (int col = 0; col < this.size; col++) {
                tiles.get(row).add(new Tile());
            }
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }

    // 開始遊戲
    private void start() {
        Game.CONTROLS.bind();
        initialize();
        genInitTiles();
        // show();
    }

    public void game_reset() {

        if (this.score > this.highScore) {
            setHighScore(this.score);
        }

        this.score = 0;
        this.emptyTiles = this.size * this.size;
        this.gameover = false;
        this.wonOrLost = null;
        this.genNewTile = false;
        this.tiles.clear();
        
        //0816 增加
        this.previousStates.clear();
        
        initialize();
        genInitTiles();

        Grid_v9.setShowpg(false);
        Grid_v9.setShowleave(false);
        Grid_v9.setShowreset(false);
    }

    public void esc_game() {
        System.exit(0); //關閉整個Java應用程序
        //Game.WINDOW.dispose(); //關閉當前視窗
    }

    // 獲取遊戲板大小
    public int getSize() {
        return size;
    }

    // 設置遊戲板大小
    public void setSize(int size) {
        this.size = size;
    }

    // 獲取所有磁磚
    public List<List<Tile>> getTiles() {
        return tiles;
    }

    // 設置所有磁磚
    public void setTiles(List<List<Tile>> tiles) {
        this.tiles = tiles;
    }

    //獲取指定位置的磁磚
    public Tile getTileAt(int row, int col) {
        return tiles.get(row).get(col);
    }

    //設置指定位置的磁磚
    public void setTileAt(int row, int col, Tile t) {
        tiles.get(row).set(col, t);
    }

    //移除指定位置的磁磚
    public void remTileAt(int row, int col) {
        tiles.get(row).remove(col);
    }

    //獲取分數
    public int getScore() {
        return score;
    }

    //合併磁磚
    private List<Tile> mergeTiles(List<Tile> sequence,String direction) {
		if (direction.equals("up") || direction.equals("left")){
			for (int l = 0; l < sequence.size() - 1; l++) {
				if (sequence.get(l).getValue() == sequence.get(l + 1).getValue()) {
					int value;
					if ((value = sequence.get(l).merging()) == 2048) {
						gameover = true;
					}
					score += value;
					setHighScore(score);  // 更新高分
					sequence.remove(l + 1);
					genNewTile = true; // board has changed its state
					emptyTiles++;
					l--;
				}
			}	
        
			for (int l = 0; l < sequence.size() - 2; l++) {
				if (sequence.get(l).getValue() == sequence.get(l + 1).getValue() && 
					sequence.get(l).getValue() == sequence.get(l + 2).getValue()) {
					int value;
					if ((value = sequence.get(l).merging()) == 2048) {
						gameover = true;
					}
					score += value;
					setHighScore(score); // 更新高分
					sequence.remove(l + 1); // 移除第二個磚塊
					sequence.remove(l + 1); // 移除第三個磚塊 (因為已經移除一個，所以索引加一)
					genNewTile = true; // board has changed its state
					emptyTiles++;
					l--; // 調整索引以檢查新的相鄰磚塊
				}
			}
        }
		else if (direction.equals("down") || direction.equals("right")){
			for (int l = sequence.size() - 1; l > 0; l--) {
            // 處理兩個相鄰的磚塊合併
				if (sequence.get(l).getValue() == sequence.get(l - 1).getValue()) {
					int value;
					if ((value = sequence.get(l).merging()) == 2048) {
						gameover = true;
					}
					score += value;
					setHighScore(score); // 更新高分
					sequence.remove(l - 1); // 移除合併的磚塊
					genNewTile = true; // board has changed its state
					emptyTiles++;
					l--; // 調整索引
				}
			}
		}
			// 處理三個相同的磚塊合併
			for (int l = sequence.size() - 1; l > 1; l--) {
				if (sequence.get(l).getValue() == sequence.get(l - 1).getValue() &&
					sequence.get(l).getValue() == sequence.get(l - 2).getValue()) {
					int value;
					if ((value = sequence.get(l).merging()) == 2048) {
						gameover = true;
					}
					score += value;
					setHighScore(score); // 更新高分
					sequence.remove(l - 1); // 移除第二個磚塊
					sequence.remove(l - 1); // 移除第三個磚塊
					genNewTile = true; // board has changed its state
					emptyTiles++;
					l--; // 調整索引
            }
        }
		
        return sequence;
    }
    
//    private List<Tile> mergeTiles(List<Tile> sequence) {
//        for (int l = 0; l < sequence.size() - 1; l++) { 
//        	// 如果當前 Tile 和下一個 Tile 的數值相同，則進行合併
//            if (sequence.get(l).getValue() == sequence.get(l + 1).getValue()) {
//                int value;
//                if ((value = sequence.get(l).merging()) == 2048) {
//                    gameover = true;
//                }
//                score += value;
//                setHighScore(score);  // 更新最高分數
//                // 從序列中移除已合併的下一個 Tile
//                sequence.remove(l + 1);
//                // 設置標誌，表示棋盤狀態已改變，需要生成新 Tile
//                genNewTile = true; 
//                // 增加空格子數量，因為合併後一個格子變空了
//                emptyTiles++;
//                l--;
//            }
//        }
//        return sequence;
//    }
    
    //在合併後的序列前添加空磁磚
    private List<Tile> addEmptyTilesFirst(List<Tile> merged) {
        for (int k = merged.size(); k < size; k++) {
            merged.add(0, new Tile());
        }
        return merged;
    }

    //在合併後的序列後添加空磁磚
    private List<Tile> addEmptyTilesLast(List<Tile> merged) { // boolean last/first
        for (int k = merged.size(); k < size; k++) {
            merged.add(k, new Tile());
        }
        return merged;
    }

    //移除行中的空磁磚
    private List<Tile> removeEmptyTilesRows(int row) {

        List<Tile> moved = new ArrayList<>();

        for (int col = 0; col < size; col++) {
            if (!getTileAt(row, col).isEmpty()) { // NOT empty
                moved.add(getTileAt(row, col));
            }
        }

        return moved;
    }

    //移除列中的空磁磚
    private List<Tile> removeEmptyTilesCols(int row) {

        List<Tile> moved = new ArrayList<>();

        for (int col = 0; col < size; col++) {
            if (!getTileAt(col, row).isEmpty()) { // NOT empty
                moved.add(getTileAt(col, row));
            }
        }

        return moved;
    }

    //將行設置回遊戲板
    private List<Tile> setRowToBoard(List<Tile> moved, int row) {
        for (int col = 0; col < tiles.size(); col++) {
            if (moved.get(col).hasMoved(row, col)) {
                genNewTile = true;
            }
            setTileAt(row, col, moved.get(col));
        }

        return moved;
    }

    // 將列設置回遊戲板
    private List<Tile> setColToBoard(List<Tile> moved, int row) {
        for (int col = 0; col < tiles.size(); col++) {
            if (moved.get(col).hasMoved(col, row)) {
                genNewTile = true;
            }
            setTileAt(col, row, moved.get(col));
        }

        return moved;
    }

    // 向上移動磁磚
    public void moveUp() {
        savePreviousState();
        List<Tile> moved;
        direction = "up";

        for (int row = 0; row < size; row++) {

            moved = removeEmptyTilesCols(row);
            //moved = mergeTiles(moved);
            moved = mergeTiles(moved,direction);
            moved = addEmptyTilesLast(moved);
            moved = setColToBoard(moved, row);
        }

    }

    //向下移動磁磚
    public void moveDown() {
        savePreviousState();
        List<Tile> moved;
        direction = "down";

        for (int row = 0; row < size; row++) {

            moved = removeEmptyTilesCols(row);
            moved = mergeTiles(moved,direction);
            //moved = mergeTiles(moved);
            moved = addEmptyTilesFirst(moved);
            moved = setColToBoard(moved, row);

        }

    }

    //向左移動磁磚
    public void moveLeft() {
        savePreviousState();
        List<Tile> moved;
        direction = "left";

        for (int row = 0; row < size; row++) {

            moved = removeEmptyTilesRows(row);
            moved = mergeTiles(moved,direction);
            //moved = mergeTiles(moved);
            moved = addEmptyTilesLast(moved);
            moved = setRowToBoard(moved, row);

        }

    }

    //向右移動磁磚
    public void moveRight() {
        savePreviousState();
        List<Tile> moved;
        direction = "right";

        for (int row = 0; row < size; row++) {

            moved = removeEmptyTilesRows(row);
            //moved = mergeTiles(moved);
            moved = mergeTiles(moved,direction);
            moved = addEmptyTilesFirst(moved);
            moved = setRowToBoard(moved, row);

        }

    }

    // 檢查遊戲是否結束
    public void isGameOver() {
        if (gameover) {
            // you won (the board has tile 2048)
            // end(true);
            setWonOrLost("Won");
        } else {
            if (isFull()) {
                if (!isMovePossible()) {
                    // 你輸了（遊戲板已滿且無法合併磁磚）
                    // end(false);
                    setWonOrLost("Lost");
                }

            } else {
                newRandomTile(); // 遊戲繼續
            }
        }
    }

    // 檢查遊戲板是否已滿
    private boolean isFull() {
        return emptyTiles == 0;
    }

    // 檢查是否可以移動
    private boolean isMovePossible() {
        // 檢查水平方向
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 1; col++) {
                if (getTileAt(row, col).getValue() == getTileAt(row, col + 1).getValue()) {
                    return true;
                }
            }
        }
        // 檢查垂直方向
        for (int row = 0; row < size - 1; row++) {
            for (int col = 0; col < size; col++) {
                if (getTileAt(col, row).getValue() == getTileAt(col, row + 1).getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    // 生成初始磁磚
    private void genInitTiles() {
        for (int i = 0; i < initTiles; i++) {
            genNewTile = true;
            newRandomTile();
        }
    }

    // 生成隨機磁磚
    private void newRandomTile() {
        if (genNewTile) {
            int row;
            int col;
            int value = Math.random() < 0.95 ? 2 : 4;
            do {
                row = (int) (Math.random() * 4);
                col = (int) (Math.random() * 4);
            } while (getTileAt(row, col).getValue() != 0);
            setTileAt(row, col, new Tile(value, row, col));
            emptyTiles--;
            genNewTile = false;
        }
    }

    // 獲取遊戲結果（勝利或失敗）
    public String getWonOrLost() {
        return wonOrLost;
    }

    // 設置遊戲結果
    public void setWonOrLost(String wonOrLost) {
        this.wonOrLost = wonOrLost;
    }


    //20240722 add 0810 new
    public void savePreviousState() {
        GameState currentState = new GameState(
                deepCopyTiles(tiles),
                score,
                gameover,
                wonOrLost
        );
        previousStates.push(currentState);
        limitUndoSteps();
    }
    
    //0816增加
    // 檢查是否有可用的撤銷次數
    public boolean isUndoAvailable() {
        return !previousStates.isEmpty(); // 如果堆疊不為空，則返回 true
    }

    private List<List<Tile>> deepCopyTiles(List<List<Tile>> original) {
        List<List<Tile>> copy = new ArrayList<>();
        for (List<Tile> row : original) {
            List<Tile> newRow = new ArrayList<>();
            for (Tile tile : row) {
                newRow.add(new Tile(tile));
            }
            copy.add(newRow);
        }
        return copy;
    }

    private void limitUndoSteps() {
        while (previousStates.size() > MAX_UNDO_STEPS) {
            previousStates.remove(0);
        }
    }

    public boolean hasPreviousState() {
        return !previousStates.isEmpty();
    }


    //0810 new score , title ,winlost update
    public void restorePreviousState() {
        if (hasPreviousState()) {
            GameState previousState = previousStates.pop();
            tiles = previousState.boardState;
            score = previousState.stateScore;
            genNewTile = false;
            emptyTiles = countEmptyTiles();
            gameover = previousState.stateGameover;
            wonOrLost = previousState.stateWonOrLost;
            genNewTile = false;
        }
    }

    private int countEmptyTiles() {
        int count = 0;
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                if (tile.isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    // 20240722 add
    private class GameState {
        private List<List<Tile>> boardState;
        private int stateScore;

        //0810 add

        private boolean stateGameover;
        private String stateWonOrLost;

        public GameState(List<List<Tile>> boardState, int score, boolean gameover, String wonOrLost) {
            this.boardState = new ArrayList<>();
            for (List<Tile> row : boardState) {
                this.boardState.add(new ArrayList<>(row));
            }
            this.stateScore = score;
        }
    }

}