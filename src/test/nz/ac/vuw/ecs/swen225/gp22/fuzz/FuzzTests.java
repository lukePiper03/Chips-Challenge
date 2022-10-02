package test.nz.ac.vuw.ecs.swen225.gp22.fuzz;
import nz.ac.vuw.ecs.swen225.gp22.app.Chips;
import nz.ac.vuw.ecs.swen225.gp22.app.Controller;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;


public class FuzzTests {
    @Test
    public void FuzzTest1() {

        SwingUtilities.invokeLater(Chips::new);
        // create new bot
        Robot bot;
        try {
            bot = new Robot();
            //HashMap<String, Integer> controls = cont.getKeyset();
            int l;
            for (int i = 0; i < 50; i++) {
                l = randomKey();
                System.out.println(l);
                bot.keyPress(l);
            }
        } catch (AWTException|IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Bad key");
            fail();
        }



        assertTrue(true);
    }

    @Test
    public void FuzzTest2() {
        for (int i = 0; i < 50; i++) {
            SwingUtilities.invokeLater(Chips::new);
            // create new bot
            Robot bot;
            try {
                bot = new Robot();
                //HashMap<String, Integer> controls = cont.getKeyset();
                int l;
                for (int j = 0; j < 50; j++) {
                    l = randomKey();
                    System.out.println(l);
                    bot.keyPress(l);
                }
            } catch (AWTException | IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println("Bad key");
                fail();
            }
        }

        assertFalse(false);
    }

    int randomKey() {
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
}
