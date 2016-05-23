package aima.core.environment.horsequeen;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {

	private Timer timer = new Timer();
	private int seconds = 0;

	// Clase interna que funciona como contador
	class Counter extends TimerTask {
		public void run() {
			seconds++;
		}
	}

	// Crea un timer, inicia segundos a 0 y comienza a contar
	public void Count() {
		this.seconds = 0;
		timer = new Timer();
		timer.schedule(new Counter(), 0, 1000);
	}

	// Detiene el contador
	public void Stop() {
		timer.cancel();
	}

	// Metodo que retorna los segundos transcurridos
	public int getSeconds() {
		return this.seconds;
	}

}
