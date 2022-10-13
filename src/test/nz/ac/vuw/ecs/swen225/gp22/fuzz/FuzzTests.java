package test.nz.ac.vuw.ecs.swen225.gp22.fuzz;
import nz.ac.vuw.ecs.swen225.gp22.app.Chips;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class FuzzTests {
    /*public FuzzTests(){
        assertTimeoutPreemptively(Duration.ofSeconds(10), this::FuzzTest1);
        assertTimeoutPreemptively(Duration.ofSeconds(10), this::FuzzTest2);
    }*/

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void FuzzTest1() throws AWTException {
        SwingUtilities.invokeLater(Chips::new);
        try {
            //setup phase1
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
            MyRobot bot = new MyRobot();
            bot.delay(1000);
            bot.mouseMove((int) ((screenSize.getWidth() / 2) - 230), (int) ((screenSize.getHeight() / 2) + 120));
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            while(true){
                bot.keyPress(randomKeystroke());
               //testGame.controller.keyPressed(randomKey(testGame));
            }
        } catch (AWTException|IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bad key");
            fail();
        }
    }

    @Test
    public void FuzzTest2() throws AWTException {
        SwingUtilities.invokeLater(Chips::new);
        try {
            //setup phase1
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, false);
            MyRobot bot = new MyRobot();
            bot.delay(1000);
            bot.mouseMove((int) ((screenSize.getWidth() / 2) - 230), (int) ((screenSize.getHeight() / 2) +  220));
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            while(true){
                bot.keyPress(randomKeystroke());
                //testGame.controller.keyPressed(randomKey(testGame));
            }
        } catch (AWTException|IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bad key");
            fail();
        }
    }

    /*KeyEvent randomKey(Chips c) {
        HashMap<String,Integer> keys = c.controller.getKeyset();
        String s = "wasd";
        int l = (int)(Math.random()* s.length());
        char ch = s.charAt(l);
        return keys.get(String.valueOf(ch));
    }*/

    int randomKeystroke() {
        // generate a random key
        // Only movement keys
        String s = "wasd";
        //  Full set of inputs
        //String s = "abcdefghijklmnopqrstuvwxyz`0123456789-=!@#$^&*()_+TN[]\\;:" +
        //        "'\",./ ";
        int l = (int)(Math.random()* s.length());
        char ch = s.charAt(l);


        switch (ch) {
            case 'a': return KeyEvent.VK_A;
            case 'b': return KeyEvent.VK_B;
            case 'c': return KeyEvent.VK_C;
            case 'd': return KeyEvent.VK_D;
            case 'e': return KeyEvent.VK_E;
            case 'f': return KeyEvent.VK_F;
            case 'g': return KeyEvent.VK_G;
            case 'h': return KeyEvent.VK_H;
            case 'i': return KeyEvent.VK_I;
            case 'j': return KeyEvent.VK_J;
            case 'k': return KeyEvent.VK_K;
            case 'l': return KeyEvent.VK_L;
            case 'm': return KeyEvent.VK_M;
            case 'n': return KeyEvent.VK_N;
            case 'o': return KeyEvent.VK_O;
            case 'p': return KeyEvent.VK_P;
            case 'q': return KeyEvent.VK_Q;
            case 'r': return KeyEvent.VK_R;
            case 's': return KeyEvent.VK_S;
            case 't': return KeyEvent.VK_T;
            case 'u': return KeyEvent.VK_U;
            case 'v': return KeyEvent.VK_V;
            case 'w': return KeyEvent.VK_W;
            case 'x': return KeyEvent.VK_X;
            case 'y': return KeyEvent.VK_Y;
            case 'z': return KeyEvent.VK_Z;
            case '`': return KeyEvent.VK_BACK_QUOTE;
            case '0': return KeyEvent.VK_0;
            case '1': return KeyEvent.VK_1;
            case '2': return KeyEvent.VK_2;
            case '3': return KeyEvent.VK_3;
            case '4': return KeyEvent.VK_4;
            case '5': return KeyEvent.VK_5;
            case '6': return KeyEvent.VK_6;
            case '7': return KeyEvent.VK_7;
            case '8': return KeyEvent.VK_8;
            case '9': return KeyEvent.VK_9;
            case '-': return KeyEvent.VK_MINUS;
            case '=': return KeyEvent.VK_EQUALS;
            case '!': return KeyEvent.VK_EXCLAMATION_MARK;
            case '@': return KeyEvent.VK_AT;
            case '#': return KeyEvent.VK_NUMBER_SIGN;
            case '$': return KeyEvent.VK_DOLLAR;
            case '^': return KeyEvent.VK_CIRCUMFLEX;
            case '&': return KeyEvent.VK_AMPERSAND;
            case '*': return KeyEvent.VK_ASTERISK;
            case '(': return KeyEvent.VK_LEFT_PARENTHESIS;
            case ')': return KeyEvent.VK_RIGHT_PARENTHESIS;
            case '_': return KeyEvent.VK_UNDERSCORE;
            case '+': return KeyEvent.VK_PLUS;
            case 'T': return KeyEvent.VK_TAB;
            case 'N': return KeyEvent.VK_ENTER;
            case '[': return KeyEvent.VK_OPEN_BRACKET;
            case ']': return KeyEvent.VK_CLOSE_BRACKET;
            case '\\': return KeyEvent.VK_BACK_SLASH;
            case ';': return KeyEvent.VK_SEMICOLON;
            case ':': return KeyEvent.VK_COLON;
            case '\'': return KeyEvent.VK_QUOTE;
            case '"': return KeyEvent.VK_QUOTEDBL;
            case ',': return KeyEvent.VK_COMMA;
            case '.': return KeyEvent.VK_PERIOD;
            case '/': return KeyEvent.VK_SLASH;
            case ' ': return KeyEvent.VK_SPACE;
            default:
                throw new IllegalArgumentException("Cannot type character " + ch);
        }
    }
    class MyRobot extends Robot{

        public MyRobot() throws AWTException {
        }
        @Override
        public void keyPress(int keycode){
            super.keyPress(keycode);
            delay(200);
            super.keyRelease(keycode);
        }
    }
}
