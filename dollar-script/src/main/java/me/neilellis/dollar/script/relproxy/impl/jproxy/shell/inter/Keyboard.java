/*
 * Copyright (c) 2014-2015 Neil Ellis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.neilellis.dollar.script.relproxy.impl.jproxy.shell.inter;

import me.neilellis.dollar.script.relproxy.RelProxyException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static java.awt.event.KeyEvent.*;

public abstract class Keyboard {
    @NotNull protected final Robot robot;

    public Keyboard() {
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            throw new RelProxyException(ex);
        }
    }

    @NotNull public static Keyboard create(Charset cs) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) return new WindowUnicodeKeyboard(cs);
        else if (osName.contains("os x"))
            return new MacOSXUnicodeKeyboard(cs);// https://developer.apple.com/library/mac/technotes/tn2002/tn2110.html
        else return new LinuxUnicodeKeyboard(cs);
    }

    public static int getUnicodeInt(@NotNull Charset cs, char character) {
        ByteBuffer buffer = cs.encode("" + character);

        byte b = buffer.get();
        int bi = b & 0x000000FF;
        return bi;
    }

    public void type(@NotNull CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

    public boolean type(char character) {

        // He quitado todos los símbolos que son susceptibles de cambiar según el teclado pues hay que acertar exactamente la combinación de teclas del teclado
        // concreto o da error, ej no vale emitir VK_COLON únicamente si en el teclado concreto es necesario un SHIFT
        // En la clase derivada se procesan los caracteres no contemplados aquí

        switch (character) {
            case 'a':
                doType(VK_A);
                break;
            case 'b':
                doType(VK_B);
                break;
            case 'c':
                doType(VK_C);
                break;
            case 'd':
                doType(VK_D);
                break;
            case 'e':
                doType(VK_E);
                break;
            case 'f':
                doType(VK_F);
                break;
            case 'g':
                doType(VK_G);
                break;
            case 'h':
                doType(VK_H);
                break;
            case 'i':
                doType(VK_I);
                break;
            case 'j':
                doType(VK_J);
                break;
            case 'k':
                doType(VK_K);
                break;
            case 'l':
                doType(VK_L);
                break;
            case 'm':
                doType(VK_M);
                break;
            case 'n':
                doType(VK_N);
                break;
            case 'o':
                doType(VK_O);
                break;
            case 'p':
                doType(VK_P);
                break;
            case 'q':
                doType(VK_Q);
                break;
            case 'r':
                doType(VK_R);
                break;
            case 's':
                doType(VK_S);
                break;
            case 't':
                doType(VK_T);
                break;
            case 'u':
                doType(VK_U);
                break;
            case 'v':
                doType(VK_V);
                break;
            case 'w':
                doType(VK_W);
                break;
            case 'x':
                doType(VK_X);
                break;
            case 'y':
                doType(VK_Y);
                break;
            case 'z':
                doType(VK_Z);
                break;
            case 'A':
                doType(VK_SHIFT, VK_A);
                break;
            case 'B':
                doType(VK_SHIFT, VK_B);
                break;
            case 'C':
                doType(VK_SHIFT, VK_C);
                break;
            case 'D':
                doType(VK_SHIFT, VK_D);
                break;
            case 'E':
                doType(VK_SHIFT, VK_E);
                break;
            case 'F':
                doType(VK_SHIFT, VK_F);
                break;
            case 'G':
                doType(VK_SHIFT, VK_G);
                break;
            case 'H':
                doType(VK_SHIFT, VK_H);
                break;
            case 'I':
                doType(VK_SHIFT, VK_I);
                break;
            case 'J':
                doType(VK_SHIFT, VK_J);
                break;
            case 'K':
                doType(VK_SHIFT, VK_K);
                break;
            case 'L':
                doType(VK_SHIFT, VK_L);
                break;
            case 'M':
                doType(VK_SHIFT, VK_M);
                break;
            case 'N':
                doType(VK_SHIFT, VK_N);
                break;
            case 'O':
                doType(VK_SHIFT, VK_O);
                break;
            case 'P':
                doType(VK_SHIFT, VK_P);
                break;
            case 'Q':
                doType(VK_SHIFT, VK_Q);
                break;
            case 'R':
                doType(VK_SHIFT, VK_R);
                break;
            case 'S':
                doType(VK_SHIFT, VK_S);
                break;
            case 'T':
                doType(VK_SHIFT, VK_T);
                break;
            case 'U':
                doType(VK_SHIFT, VK_U);
                break;
            case 'V':
                doType(VK_SHIFT, VK_V);
                break;
            case 'W':
                doType(VK_SHIFT, VK_W);
                break;
            case 'X':
                doType(VK_SHIFT, VK_X);
                break;
            case 'Y':
                doType(VK_SHIFT, VK_Y);
                break;
            case 'Z':
                doType(VK_SHIFT, VK_Z);
                break;
            case '`':
                doType(VK_BACK_QUOTE);
                break;
            case '0':
                doType(VK_0);
                break;
            case '1':
                doType(VK_1);
                break;
            case '2':
                doType(VK_2);
                break;
            case '3':
                doType(VK_3);
                break;
            case '4':
                doType(VK_4);
                break;
            case '5':
                doType(VK_5);
                break;
            case '6':
                doType(VK_6);
                break;
            case '7':
                doType(VK_7);
                break;
            case '8':
                doType(VK_8);
                break;
            case '9':
                doType(VK_9);
                break;
        /*
        case '-': doType(VK_MINUS); break;
        case '=': doType(VK_EQUALS); break;
        case '~': doType(VK_SHIFT, VK_BACK_QUOTE); break;
        case '!': doType(VK_EXCLAMATION_MARK); break;
        case '@': doType(VK_AT); break;
        case '#': doType(VK_NUMBER_SIGN); break;
        case '$': doType(VK_DOLLAR); break;
        case '%': doType(VK_SHIFT, VK_5); break;
        case '^': doType(VK_CIRCUMFLEX); break;
        case '&': doType(VK_AMPERSAND); break;
        case '*': doType(VK_ASTERISK); break;
        case '(': doType(VK_LEFT_PARENTHESIS); break;
        case ')': doType(VK_RIGHT_PARENTHESIS); break;
        case '_': doType(VK_UNDERSCORE); break;
        case '+': doType(VK_PLUS); break;
        case '\t': doType(VK_TAB); break;
        case '\n': doType(VK_ENTER); break;
        case '[': doType(VK_OPEN_BRACKET); break;
        case ']': doType(VK_CLOSE_BRACKET); break;
        case '\\': doType(VK_BACK_SLASH); break;
        case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
        case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
        case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
        case ';': doType(VK_SEMICOLON); break;
        case ':': doType(VK_COLON); break;
        case '\'': doType(VK_QUOTE); break;
        case '"': doType(VK_QUOTEDBL); break;
        case ',': doType(VK_COMMA); break;
        case '<': doType(VK_LESS); break;
        case '.': doType(VK_PERIOD); break;
        case '>': doType(VK_GREATER); break;
        case '/': doType(VK_SLASH); break;
        case '?': doType(VK_SHIFT, VK_SLASH); break;
        */
            case ' ':
                doType(VK_SPACE);
                break;
            default:
                return false;
        }

        return true;
    }

    protected void doType(int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    protected void doType(@NotNull int... keyCodes) {
        doTypeArr(keyCodes);
    }

    private void doTypeArr(@NotNull int[] keyCodes) {
        int length = keyCodes.length;
        if (length == 1) {
            doType(keyCodes[0]);
        } else // 2
        {
            robot.keyPress(keyCodes[0]);
            robot.keyPress(keyCodes[1]);

            robot.keyRelease(keyCodes[1]);
            robot.keyRelease(keyCodes[0]);
        }
    }
}