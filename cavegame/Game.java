package dev.codenmore.cavegame;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.codenmore.cavegame.display.Display;
import dev.codenmore.cavegame.gfx.Assets;
import dev.codenmore.cavegame.input.KeyManager;
import dev.codenmore.cavegame.states.GameState;
import dev.codenmore.cavegame.states.MenuState;
import dev.codenmore.cavegame.states.State;

public class Game implements Runnable {

	private Display display;
	public int width, height;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
        
        
        //states
        private State gameState;
        private State menuState;
	
        
        private KeyManager keyManager;
	public Game(String title, int width, int height){
		this.width = width;
		this.height = height;
		this.title = title;
                keyManager= new KeyManager();
	}
	
	private void init(){
		display = new Display(title, width, height);
                display.getFrame().addKeyListener(keyManager);
		Assets.init();
                gameState=new GameState(this);
                menuState=new MenuState(this);
                State.setState(gameState);
	}
	
	private void tick(){
            keyManager.tick();
		if(State.getState()!=null){
                    State.getState().tick();
                }
	}
	
	private void render(){
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		g.clearRect(0, 0, width, height);
		//Draw Here!
		if(State.getState()!=null){
                    State.getState().render(g);
                }
		
		//End Drawing!
		bs.show();
		g.dispose();
	}
	
	public void run(){
		
            
            
		init();
		
                int fps=60;///tick and render method 1s e 60 br call hobe
                double timePerTick=1000000000/fps;///maximum time koto somoy run hobe
                double delta=0;
                long now;
                long lastTime=System.nanoTime();
                long timer=0;
                int ticks=0;
                
                
                
		while(running){
                    
                    now=System.nanoTime();
                    delta +=(now-lastTime)/timePerTick;
                    ///koto somoy par hoise/maximum time
                    timer +=now-lastTime;
                    lastTime=now;
                    
                     
                    if(delta>=1){
                        tick();
			render();
                        ticks++;
                        delta--;
                    }
			
                    if(timer>=1000000000){
                        System.out.println(" Ticks and Frames: "+ticks);
                        ticks=0;
                        timer=0;
                    }
		}
		
		stop();
		
	}
        
        public KeyManager getKeyManager(){
            return keyManager;
        }
	
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

