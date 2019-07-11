package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.LinkedList;
import java.util.List;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {
    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        //System.out.println(board.toString());//отрисовка всякой инфы
        //System.out.println(board.getBarriers());
        int xHead = 0;
        int yHead = 0;
        int xGA = 0;
        int yGA = 0;
// координаты головы
        Point head = board.getHead();
        if(head != null) {
            xHead = head.getX();
            yHead = head.getY();
        }
// координаты яблока(пока для одного, дальше допилить для ближайшего)
        List<Point> GA = board.getApples();
        if(GA != null) {
            xGA = GA.get(0).getX();
            yGA = GA.get(0).getY();
        }
        String str = Direction.STOP.toString();

        //System.out.println("xHead : " +xHead + "\txGA : " + xGA + "\n" +
        //        "yHead : " +yHead + "\tyGA : " + yGA);

        //флаги для возможных ходов
        boolean[] trueDirection = new boolean[4];
        for(int i =0;i<trueDirection.length; i++){
           trueDirection[i] = true;
        }

        // получаем возможные ходы(x y возможных ходов)
        List<Point> headDirection = new LinkedList<>();
        for(Direction direction : Direction.onlyDirections()) {
            Point head1 = board.getHead();
            head1.change(direction);
            headDirection.add(head1);
        }

        //System.out.println(headDirection);

        // производим сравнение возможных ходов с барьерами, отсекаем лишнее(через флаги)
        List<Point> barriers = board.getBarriers();
            // 4 flags
        int k =0;
        for(Point headPoint : headDirection){
            //возможный ход совпал с барьером
            for(Point point : barriers){
                if(headPoint.equals(point)){
                    trueDirection[k] = false;
                    break;
                    }
            }
            k++;
        }

        // с учетом барьеров и направления на яблоко
        if((xHead>xGA)&&(trueDirection[0])){
            str = Direction.LEFT.toString();
        }else
        if((xHead<xGA)&&(trueDirection[1])){
            str = Direction.RIGHT.toString();
        }else
        if((yHead<yGA)&&(trueDirection[2])) {
            str = Direction.UP.toString();
        }else
        if((yHead>yGA)&&(trueDirection[3])) {
            str = Direction.DOWN.toString();
        }else
        if((trueDirection[0])){
            str = Direction.LEFT.toString();
        }else
        if((trueDirection[1])){
            str = Direction.RIGHT.toString();
        }else
        if((trueDirection[2])) {
            str = Direction.UP.toString();
        }else
            str = Direction.DOWN.toString();


        for(int ii =0 ;ii <trueDirection.length; ii++){
            System.out.println(trueDirection[ii]);
        }

        return str;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://10.6.222.47/fight/board/player/zgd5121zlss0w7ljy7dy?code=498524438219411614",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
