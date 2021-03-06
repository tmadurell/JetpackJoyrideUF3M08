package com.mygdx.jetpackjoyrideuf3m08;


import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;


import java.util.Iterator;

public class GameScreen implements Screen {
    final Character game;

    Texture birdImage;
    Texture backgroundImage;
    Texture pipeUpImage;
    Texture pipeDownImage;

    Texture pauseT;
    Rectangle pause;

    OrthographicCamera camera;

    Rectangle player;
    Array<Rectangle> obstacles;
    long lastObstacleTime;

    float speedy;
    float gravity;
    boolean dead;

    float score;

    Sound flapSound;
    Sound failSound;

    Music backgroundMusic;
    boolean playMusic = false;

    public GameScreen(final Character gam) {
        this.game = gam;

        score = 0;

        // load the pause
        pauseT = new Texture(Gdx.files.internal("pause.png"));
        pause = new Rectangle();
        pause.y = 375;
        pause.x = 700;
        pause.height = 64;
        pause.width = 64;

        // load the images
        backgroundImage = new Texture(Gdx.files.internal("backgroundlaboratory.png"));
        birdImage = new Texture(Gdx.files.internal("barry.png"));
        pipeUpImage = new Texture(Gdx.files.internal("laser_ele.png"));
        pipeDownImage = new Texture(Gdx.files.internal("laser_eleh.png"));

        // load the sound effects
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        backgroundMusic.setLooping(true);

//        flapSound = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
//        failSound = Gdx.audio.newSound(Gdx.files.internal("fail.wav"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);


        // create a Rectangle to logically represent the player
        player = new Rectangle();
        player.x = 50;
        player.y = 6;
        player.width = 30;
        player.height = 50;

        speedy = 0;
        gravity = 850f;

        dead = false;

        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Rectangle>();
        spawnObstacle();


    }

    private void spawnObstacle() {

        // Calcula la al??ada de l'obstacle aleat??riament
        float holey = MathUtils.random(50, 230);

        // Crea dos obstacles: Una tuber??a superior i una inferior
        Rectangle pipe1 = new Rectangle();
        pipe1.x = 1000;
        pipe1.y = holey - 230;
        pipe1.width = 31;
        pipe1.height = 81;
        obstacles.add(pipe1);

        Rectangle pipe2 = new Rectangle();
        pipe2.x = 1100;
        pipe2.y = holey + 200;
        pipe2.width = 80;
        pipe2.height = 50;
        obstacles.add(pipe2);

        lastObstacleTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {



        //=====RENDER=====/
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        // begin a new batch
        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0);
        game.batch.draw(birdImage, player.x, player.y);
        // Dibuixa els obstacles: Els parells son tuberia inferior,
        //els imparells tuberia superior
        for(int i = 0; i < obstacles.size; i++)
        {
            game.batch.draw(
                    i % 2 == 0 ? pipeUpImage : pipeUpImage,
                    obstacles.get(i).x, obstacles.get(i).y);
        }
        for(int i = 0; i < obstacles.size; i++)
        {
            game.batch.draw(
                    i % 3 == 0 ? pipeDownImage : pipeDownImage,
                    obstacles.get(i).x, obstacles.get(i).y);
        }
        game.font.draw(game.batch, "Score: " + (int)score, 10, 470);
        game.batch.end();

        //=====LOGICA=====/
        // process user input
        //if (Gdx.input.justTouched()) {
        if(Gdx.input.isTouched()){
            speedy = 200f;
//            flapSound.play();
        }

        //Actualitza la posici?? del jugador amb la velocitat vertical
        player.y += speedy * Gdx.graphics.getDeltaTime();
        //Actualitza la velocitat vertical amb la gravetat
        speedy -= gravity * Gdx.graphics.getDeltaTime();
        //La puntuaci?? augmenta amb el temps de joc
        score += Gdx.graphics.getDeltaTime();

        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.y > 480 - 145)
            player.y = 480 - 145;

        if (player.y < 5)
            player.y = 5;

        // if (player.y < 0 - 45) {
        // dead = true;
        // }

        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000)
            spawnObstacle();

        // Mou els obstacles. Elimina els que estan fora de la pantalla
        // Comprova si el jugador colisiona amb un obstacle,
        // llavors game over
        Iterator<Rectangle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Rectangle tuberia = iter.next();
            tuberia.x -= 200 * Gdx.graphics.getDeltaTime();
            if (tuberia.x < -64)
                iter.remove();
            if (tuberia.overlaps(player)) {
                dead = true;
            }
        }


        backgroundMusic.setVolume(0.2f);
        if (!backgroundMusic.isLooping()) backgroundMusic.setLooping(true);
        if (!playMusic) {
            backgroundMusic.play();
            playMusic = true;
        }

        if(dead)
        {
//            failSound.play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore)
                game.topScore = game.lastScore;
            game.setScreen(new com.mygdx.jetpackjoyrideuf3m08.GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
        backgroundMusic.dispose();
        backgroundImage.dispose();
        birdImage.dispose();
        pipeDownImage.dispose();
        pipeUpImage.dispose();
//        failSound.dispose();
//        flapSound.dispose();
    }
}