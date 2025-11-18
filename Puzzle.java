import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Puzzle{
	private static final int WIDTH = 256 + 16; //画面横幅
	private static final int HEIGHT = 256 + 38; //画面縦幅
	
	private static GridInfo GInfo; //グリッドクラス
	
	public static void main(String[] args) {
		GameFrame gameFrame = new GameFrame();
		gameFrame.setTitle("15Puzzle");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(WIDTH, HEIGHT );
		gameFrame.setVisible(true);
	}
}

class GameFrame extends JFrame implements MouseListener{
	private static final int GAME_WAIT = 0; //ゲーム状態フラグ定数（タイトル画面時）
	private static final int GAME_ING = 1;//ゲーム状態フラグ定数（ゲーム中）
	private static final int GRID_X = 4;//ボードの横マス数
	private static final int GRID_Y = 4;//ボードの縦マス数
	private static final int GRID_WIDTH = 64;//マスの横幅
	private static final int GRID_HEIGHT = 64;//マスの縦幅
	private static int gameFlg; //ゲーム状態フラグ
	private static GridInfo GInfo; //グリッドクラス
	private static ImageIcon tileImage[];
	private static JLabel label1[];
	
	GameFrame(){
		GInfo = new GridInfo(GRID_X,GRID_Y);
		tileImage = new ImageIcon[GRID_X*GRID_Y + 1];
		label1 = new JLabel[GRID_X * GRID_Y + 1];
		
		this.getContentPane().setLayout(null);
		
		//画像の読み込み
		DecimalFormat decimalFormat = new DecimalFormat("00");
		for(int i = 1; i < GRID_X*GRID_Y; i++) {
			tileImage[i] = new ImageIcon("./images/" + decimalFormat.format(i) + ".gif");
			label1[i] = new JLabel(tileImage[i]);
			
			this.getContentPane().add(label1[i]);
		}
		//ボード上に配置
		for(int y = 0; y < GRID_Y; y++) {
			for(int x = 0; x < GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label1[GInfo.getTileNum(x, y)].setBounds(x * GRID_WIDTH,y * GRID_HEIGHT,GRID_WIDTH, GRID_HEIGHT);
				}
			}
		}
		//コンテントペインに対するマウスイベント取得開始
		this.getContentPane().addMouseListener(this);
	}
	//ゲーム初期化メソッド
	public void gameInit() {
		GInfo.shfleTile();
		gameFlg = GAME_ING;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		int clickTileX;
		int clickTileY;
		boolean blnRet;
		
		switch(gameFlg) {
			case GAME_WAIT:
				System.out.println("GAME START");
				gameInit();
				break;
			case GAME_ING:
			    //クリックされたマスを取得
			    clickTileX = (int)((e.getX()) / GRID_WIDTH);
			    clickTileY = (int)((e.getY()) / GRID_HEIGHT);

			    //コマを移動させる
			    GInfo.moveTile(clickTileX, clickTileY);

			    //コマが整列したかどうかをチェック
			    blnRet = GInfo.getGameClearFlg();
			    if (blnRet == true) {
			        gameFlg = GAME_WAIT;
			        
			        javax.swing.JOptionPane.showMessageDialog(
			                this,              // 親ウィンドウ
			                "クリアおめでとう！！🎉",  // メッセージ
			                "クリア！",                 // タイトル
			                javax.swing.JOptionPane.INFORMATION_MESSAGE
			            );
			        
			        System.out.println("GAME CLEAR");
			    }
			    break;
		}
		//描画
		for(int y = 0; y < GRID_Y; y++) {
			for(int x = 0; x < GRID_X; x++) {
				if(GInfo.getTileNum(x, y) != 0) {
					label1[GInfo.getTileNum(x, y)].setBounds(x * GRID_WIDTH, y * GRID_HEIGHT, GRID_WIDTH, GRID_HEIGHT);
				}
			}
		}
		this.setVisible(true);	
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}

class GridInfo{
	private int gridXNum;
	private int gridYNum;
	private int gridFlg[][];
	
	//コンストラクタ
	GridInfo(int xNum, int yNum){
		//引数から渡されたマスの横幅、縦幅を保存
		gridXNum = xNum;
		gridYNum = yNum;
		
		//書くマスに置かれているコマを保持する２次元配列を定義
		gridFlg = new int[gridXNum][gridYNum];
		//1~15までの数を格納
		for(int y = 0; y < gridYNum; y++) {
			for(int x = 0; x <gridXNum; x++) {
				gridFlg[y][x] = (y * gridYNum) + x + 1;
			}
		}
		//右下マスにコマがないこと意味する０を格納
		gridFlg[gridYNum - 1][gridXNum - 1] = 0;
	}
	
	//コマをシャッフルするメソッド
	public void shfleTile() {
		int clickTileX = 0;
		int clickTileY = 0;
		int clickedTileX = 0;
		int clickedTileY = 0;
		int randNum;
		boolean blnRet = false;
		
		//500回コマを移動させる
		for(int i = 0; i < 500; i++) {
			//空いているマスを取得
			clickTileX = getEmpGridXNum();
			clickTileY = getEmpGridYNum();
			
			//0~3のランダム数値の取得
			randNum = (int)(Math.random() * 4);
			
			//ランダムの数値を上下左右に対応させる
			
			switch(randNum){
			//空いているマスの右にあるコマの移動
			case 0:
				blnRet = moveTile(clickTileX +1, clickTileY);
				clickedTileX = clickTileX + 1;
				clickedTileY = clickTileY;
				break;
				//空いているマスの左にあるコマの移動
			case 1:
				blnRet = moveTile(clickTileX -1, clickTileY);
				clickedTileX = clickTileX - 1;
				clickedTileY = clickTileY;
				break;
				//空いているマスの下にあるコマの移動
			case 2:
				blnRet = moveTile(clickTileX , clickTileY + 1);
				clickedTileX = clickTileX;
				clickedTileY = clickTileY + 1;
				break;
				//空いているマスの上にあるコマの移動
			case 3:
				blnRet = moveTile(clickTileX, clickTileY - 1);
				clickedTileX = clickTileX;
				clickedTileY = clickTileY - 1;
				break;
			}
			if(blnRet == true) {
				clickTileX = clickedTileX;
				clickTileY = clickedTileY;
				moveTile(clickTileX, clickTileY);
			}
		}
	}
	//コマがおかれていない座標を返すメソッド
	public int getEmpGridXNum() {
		int rx = 0;
		for(int y = 0; y < gridYNum; y++) {
			for(int x = 0; x < gridXNum; x++) {
				if(gridFlg[y][x] == 0) {
					rx = x;
				}
			}
		}
		return rx;
	}
	public int getEmpGridYNum(){
		int ry = 0;
		for(int y = 0; y < gridYNum; y++){
			for(int x = 0; x < gridXNum; x++){
				if(gridFlg[y][x] == 0){
					ry = y;
				}
			}
		}
		return ry;
	}
	
	//空いているマスにコマを移動させるメソッド
	public boolean moveTile(int clickTileX, int clickTileY) {
		boolean blnRet;
		boolean blnExist;
		blnRet = true;
		blnExist = false;
		
		while(true){
			//右移動できるか判定
			if(clickTileX + 1 < gridXNum && clickTileX >= 0 && clickTileY >= 0 && clickTileY < gridYNum) {
				if(gridFlg[clickTileY][clickTileX + 1] == 0) {
					gridFlg[clickTileY][clickTileX + 1] = gridFlg[clickTileY][clickTileX];
					blnExist = true;
					break;
				}
			}
			//左に移動できるかの判定
			if(clickTileX - 1 >= 0 && clickTileX < gridXNum && clickTileY >= 0 && clickTileY < gridYNum) {
				if(gridFlg[clickTileY][clickTileX - 1] == 0) {
					gridFlg[clickTileY][clickTileX - 1] = gridFlg[clickTileY][clickTileX];
					blnExist = true;
					break;
				}
			}
			//下に移動できるか判定
			if(clickTileY + 1 < gridYNum && clickTileY >= 0 && clickTileX >= 0 && clickTileX < gridXNum) {
				if(gridFlg[clickTileY + 1][clickTileX] == 0) {
					gridFlg[clickTileY + 1][clickTileX] = gridFlg[clickTileY][clickTileX];
					blnExist = true;
					break;
				}
			}
			//上に移動できるか判定
			if(clickTileY - 1 >= 0 && clickTileY < gridYNum &&  clickTileX >= 0 && clickTileX < gridXNum) {
				if(gridFlg[clickTileY - 1][clickTileX] == 0) {
					gridFlg[clickTileY - 1][clickTileX] = gridFlg[clickTileY][clickTileX];
					blnExist = true;
					break;
				}
			}
			break;
		}
		//マスを空にする
		if(blnExist == true) {
			gridFlg[clickTileY][clickTileX] = 0;
		}
		return blnRet;
	}
	
	//引数で指定されたマスにおかれているコマを返すメソッド
	public int getTileNum(int x, int y) {
		return gridFlg[y][x];
	}
	//ゲームクリアを判定するメソッド
	public boolean getGameClearFlg() {
		boolean blnRet;
		blnRet = true;
		
		for(int y = 0; y < gridYNum; y++) {
			for(int x = 0; x < gridXNum; x++) {
				if( y == gridYNum - 1 && x == gridXNum - 1) {
					if(gridFlg[y][x] != 0) {
						blnRet = false; //右下のマスが空いてなかったらfalseを代入
						
					}
				}
				//数字が整列されているか判定
				else if(gridFlg[y][x] != (y * gridYNum) + x +1) {
					blnRet = false;
				}
			}
		}
		//クリアでtrueを返す
		return blnRet;
	}
}