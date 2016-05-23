package aima.core.environment.horsequeen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.framework.Metrics;

public class HorseQueen {
	private JFrame frame;
	private JPanel panel;
	public static int maxDepth;

	public JFrame buildFrame() {
		frame = new JFrame();
		frame.setTitle("HorseQueen");
		panel = new HorseQueenPanel();
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	public static void main(String[] args) {
		JFrame frame = new HorseQueen().buildFrame();
		frame.setSize(1000, 800);
		frame.setVisible(true);

	}

	public static class HorseQueenPanel extends JPanel implements
			ActionListener {

		private static final long serialVersionUID = 1L;
		JComboBox comboBoxDificult;
		JComboBox comboBoxHeuristic;
		JComboBox comboBoxField;
		JButton cleanButton;
		JButton playButton;
		JButton autoPlayButton;
		JLabel label;
		JToolBar toolBar;
		TextArea textArea;
		TextArea resultArea;
		JPanel textPanel;
		JPanel fieldPanel;
		JButton[][] arrayButton;
		String text;
		int choosenHeuristic = 0;
		int choosenField = 2;
		HorseQueenGame game;
		AdversarialSearch<Field, Movements> search;
		Field field;
		Position source;
		Position target;
		int events = 0;
		int fieldSize = 12;
		long initialTime;
		long playTime;
		long totalTime;
		Metrics metrics;

		public HorseQueenPanel() {

			this.setLayout(new BorderLayout());

			// Creamos la barra de arriba
			toolBar = new JToolBar();
			toolBar.setFloatable(false);

			// Creamos la barra de seleccion de las heuristicas
			comboBoxHeuristic = new JComboBox(
					new String[] { "Peor Heuristica", "Heuristica 1" ,"Heuristica 2", "Heuristica 3"});
			comboBoxHeuristic.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					choosenHeuristic = comboBoxHeuristic.getSelectedIndex();
				}
			});

			// Creamos la barra de seleccion de profundidad
			comboBoxDificult = new JComboBox(new String[] { "Profundidad 0",
					"Profundidad 1", "Profundidad 2", "Profundidad 3",
					"Profundidad 4" });
			comboBoxDificult.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					maxDepth = comboBoxDificult.getSelectedIndex();
				}
			});
			
			//elegimos el tamaño del tablero
			/*comboBoxField = new JComboBox(
					new String[] { "4x4", "6x6" ,"8x8", "10x10" , "12x12"});
			comboBoxField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					choosenField = comboBoxHeuristic.getSelectedIndex();
					if (choosenField == 0){
						fieldSize = 4;
					}
					if (choosenField == 1){
						fieldSize = 6;
					}
					if (choosenField == 2){
						fieldSize = 8;
					}
					if (choosenField == 3){
						fieldSize = 10;
					}
					if (choosenField == 4){
						fieldSize = 12;
					}
				}
			});*/

			// Creamos el boton de limpiar la pantalla
			cleanButton = new JButton("Desde el principio");
			cleanButton.addActionListener(this);

			// Creamos el boton de siguiente jugada

			autoPlayButton = new JButton("Propose Move");
			autoPlayButton.addActionListener(this);

			// Creamos el Panel de texto
			textPanel = new JPanel();
			textPanel.setLayout(new FlowLayout());

			textArea = new TextArea(50, 20);
			textPanel.add(textArea);

			textPanel.setBackground(Color.DARK_GRAY);

			// Definimos el area de texto del resultado

			resultArea = new TextArea(2, 2);

			// Añadimos elementos a la barra
			toolBar.add(comboBoxHeuristic);
			toolBar.add(comboBoxDificult);
		//	toolBar.add(comboBoxField);
			toolBar.add(resultArea);
			toolBar.add(cleanButton);
			toolBar.add(autoPlayButton);

			// Creamos el tablero
			fieldPanel = new JPanel(new GridLayout(fieldSize, fieldSize));

			arrayButton = new JButton[fieldSize][fieldSize];
			for (int i = 0; i < fieldSize; i++) {
				for (int j = 0; j < fieldSize; j++) {
					arrayButton[i][j] = new JButton();
					arrayButton[i][j].addActionListener(this);

					if (((j % 2 == 0 && i % 2 == 0))
							|| (j % 2 != 0 && i % 2 != 0)) {
						arrayButton[i][j].setBackground(Color.BLUE);
						fieldPanel.add(arrayButton[i][j]);

					} else {
						arrayButton[i][j].setBackground(Color.YELLOW);
						fieldPanel.add(arrayButton[i][j]);

					}
				}
			}

			// Añadimos elementos al panel
			add(toolBar, BorderLayout.NORTH);
			add(textPanel, BorderLayout.WEST);
			add(fieldPanel, BorderLayout.CENTER);

			// Creamos el nuevo juego

			game = new HorseQueenGame(textArea, fieldSize, fieldSize);

			actionPerformed(null);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			metrics = null;

			if (e == null || e.getSource() == cleanButton) {

				field = (Field) game.getInitialState();
				playTime = 0;
				totalTime = 0;
				initialTime = 0;
				
				System.out.print("--------------------------\n");
				System.out.print(field.toString());
				printField(field);

			} else if (!game.isTerminal(field)) {
				if (e.getSource() == autoPlayButton) {
					nextMove();
				} else {
					Movements action;
					for (int i = 0; i < fieldSize; i++) {
						for (int j = 0; j < fieldSize; j++) {
							if (e.getSource() == arrayButton[i][j]) {

								events++;
								if (events == 1) {
									System.out.println(events);
									if (field.getField()[i][j] != null
											&& field.getNextPlayer() == Character
													.toUpperCase(field
															.getField()[i][j]
															.getColor())) {
										source = new Position(i, j);
									} else {
										events = 0;
									}
								} else {
									System.out.println(events);
									events = 0;
									target = new Position(i, j);
									if ((source.getX() == target.getX())
											&& (source.getY() == target.getY())) {
										source = null;
										target = null;
									} else {

										List<Movements> movements = game
												.getActions(field);
										action = new Movements(source, target);
										Iterator<Movements> iterator = movements
												.iterator();
										boolean isCorrect = false;

										while (iterator.hasNext()) {
											Movements movements2 = iterator
													.next();

											if ((movements2.getOldPosition()
													.getX() == action
													.getOldPosition().getX())
													&& (movements2
															.getOldPosition()
															.getY() == action
															.getOldPosition()
															.getY())
													&& (movements2
															.getNewPosition()
															.getX() == action
															.getNewPosition()
															.getX())
													&& (movements2
															.getNewPosition()
															.getY() == action
															.getNewPosition()
															.getY())) {

												isCorrect = true;

											}
										}

										if (isCorrect) {
											field = game.getResult(field,
													action);
											printField(field);
											System.out
													.print("--------------------------\n");
											System.out.print(field.toString());
										}
									}
								}

							}
						}
					}

				}
			}
			field.analizeField();
			printField(field);
			refreshState();

		}

		private void refreshState() { // Metodo que actualiza el estado en el
										// que se encuentra el tablero despues
										// de la jugada
			String statusText;
			if (game.isTerminal(field))
				if (game.getUtility(field, "W") == 1) {
					statusText = "Felicidades...las blancas han ganado" + "\n"
							+ "Tiempo transcurrido " + totalTime + " ms";
					JOptionPane.showMessageDialog(null,
							"Ganaron las blancas" + "\n"
									+ "Tiempo transcurrido " + totalTime
									+ " ms", "FELICIDADES",
							JOptionPane.DEFAULT_OPTION);
				} else if (game.getUtility(field, "B") == 0) {
					statusText = " Felicidades...las negras han gando" + "\n"
							+ "Tiempo transcurrido " + totalTime + " ms";
					JOptionPane.showMessageDialog(null,
							"Victoria de las Negras" + "\n"
									+ "Tiempo transcurrido " + totalTime
									+ " ms", "FELICIDADES",
							JOptionPane.DEFAULT_OPTION);
				} else
					statusText = "Sin ganador";
			else if (metrics != null)
				statusText = metrics + "\n" + "Siguiente movimiento: "
						+ game.getPlayer(field) + "\n" + "Tiempo jugada "
						+ playTime + " ms";
			else
				statusText = "Siguiente movimiento: " + game.getPlayer(field);

			resultArea.setText(statusText);

		}

		public void nextMove() { // Busca segun la heuristica seleccionada

			Movements movements;

			switch (choosenHeuristic) {
			case 0:
				search = new WorstHeuristic(game);
				break;
			
			  case 1: 
				  search = new FirstHeuristic(game); 
				  break;
			  case 2: 
				  search = new SecondHeuristic(game); 
				  break;
			  case 3: 
				  search = new ThirdHeuristic(game); 
				  break;
			default:
				search = new WorstHeuristic(game);
			}

			initialTime = System.currentTimeMillis();

			movements = search.makeDecision(field);

			playTime = System.currentTimeMillis() - initialTime;

			totalTime = totalTime + playTime;

			metrics = search.getMetrics();

			if (movements != null)
				field = game.getResult(field, movements);
			if (field.noMovementsAvailable((field.getNextPlayer()))) {
				if (field.getNextPlayer() == 'B')
					field.setWinner(0);
				else
					field.setWinner(1);
			}
			printField(field);
			System.out.print("--------------------------\n");
			System.out.print(field.toString());

		}

		private void printField(Field field) {
			
			
			Piece[][] field2 = field.getField();

			for (int i = 0; i < field2.length; i++) {
				for (int j = 0; j < field2.length; j++) {

					if (field2[i][j] != null && field2[i][j] instanceof Queen) {
						arrayButton[i][j].setText(field2[i][j].getColor() + "-"
								+ field2[i][j].getStack());
					} else if (field2[i][j] != null
							&& field2[i][j] instanceof Horse)
						arrayButton[i][j].setText(field2[i][j].getColor() + "");
					else
						arrayButton[i][j].setText(null);
				}
			}
		}
		

		public static void setStatusBar(double resultValue) {
			// TODO Auto-generated method stub

		}

	}

}
