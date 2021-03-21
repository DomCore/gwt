package com.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.*;


@RemoteServiceRelativePath("gwtService")
public class Intro implements EntryPoint {

    private static class MyPopup extends PopupPanel {

        public MyPopup() {
            // PopupPanel's constructor takes 'auto-hide' as its boolean
            // parameter. If this is set, the panel closes itself
            // automatically when the user clicks outside of it.
            super(true);

            // PopupPanel is a SimplePanel, so you have to set it's widget
            // property to whatever you want its contents to be.
            Label popUp = new Label("Please select a value smaller or equal to 30.");
            popUp.setStyleName("popUp");
            setWidget(popUp);
        }
    }


    public void onModuleLoad() {

        //add panel Table
        FlexTable table = new FlexTable();
        Label label = new Label("How many number to display?");
        label.setStyleName("label");
        TextBox text = new TextBox();
        Button button = new Button("Enter");
        table.setStyleName("table");
        //add panel Intro
        VerticalPanel introPanel = new VerticalPanel();
        introPanel.setSpacing(5);
        introPanel.add(label);
        introPanel.add(text);
        introPanel.add(button);
        introPanel.getElement().setAttribute("id", "intro");

        //add panel Sort
        VerticalPanel sortPanel = new VerticalPanel();
        Button buttonSort = new Button("Sort");
        Button buttonSortDesc = new Button("Sort");
        Button buttonReset = new Button("Reset");
        sortPanel.setSpacing(5);
        sortPanel.add(buttonSort);
        sortPanel.add(buttonReset);
        sortPanel.getElement().setAttribute("id", "sort");
        buttonSort.setStyleName("sort");
        buttonSortDesc.setStyleName("sort");
        buttonReset.setStyleName("sort");
        button.setStyleName("sort");
        RootPanel.get().add(introPanel);
        button.addClickHandler(event -> {
            RootPanel.get().add(sortPanel);
            RootPanel.get().add(table);
            RootPanel.get().remove(introPanel);

            int[] array = spawnArray(text.getText(), table);
            draw(array, 0, array.length - 1, table, false);
        });

        buttonSort.addClickHandler(event -> {
            sortPanel.remove(buttonReset);
            sortPanel.remove(buttonSort);
            sortPanel.add(buttonSortDesc);
            sortPanel.add(buttonReset);
            int[] array = tableToArray(table, text.getText());
            draw(array, 0, array.length - 1, table, true);

        });

        buttonSortDesc.addClickHandler(event -> {
            sortPanel.remove(buttonReset);
            sortPanel.remove(buttonSortDesc);
            sortPanel.add(buttonSort);
            sortPanel.add(buttonReset);
            int[] array = tableToArray(table, text.getText());
            draw(array, 0, array.length - 1, table, false);
        });

        buttonReset.addClickHandler(event -> {
            Window.Location.reload();
        });

    }


    private static int[] spawnArray(String text, FlexTable table) {

        int[] array;
        array = new int[Integer.parseInt(text)];
        boolean is30Here = false;
        for (int i = 0; i < array.length; i++) {
            array[i] = ((int) (Math.random() * 1000));
            Button number = new Button(Integer.toString(array[i]));
            number.setStyleName("number");
            if (validate(number, table, text)) is30Here = true;
            table.setWidget(i % 10, i / 10, number);
        }

        if (!is30Here) {
            int place = ((int) (Math.random() * array.length));
            array[place] = ((int) (Math.random() * 30));
            Button number = new Button(Integer.toString(array[place]));
            number.setStyleName("number");
            number.addClickHandler(event -> {
                spawnArray(text, table);
            });

            table.setWidget(place % 10, place / 10, number);
        }

        return array;
    }

    public int[] tableToArray(FlexTable table, String text) {
        int[] array;
        array = new int[Integer.parseInt(text)];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(table.getText(i % 10, i / 10));
        }
        return array;
    }

    public void ArrayToTable(int[] array, FlexTable table) {

        for (int i = 0; i < array.length; i++) {
            Button number = new Button(Integer.toString(array[i]));
            number.setStyleName("number");
            table.setWidget(i % 10, i / 10, number);
        }

    }
    public static void draw(int[] array, int low, int high, FlexTable table, boolean increase) {

        // Create a new timer that calls Window.alert().
        //завершить выполнение если длина массива равна 0
        //завершить выполнение если уже нечего делить
        // выбрать опорный элемент
        // разделить на подмассивы, который больше и меньше опорного элемента
        //меняем местами
        Timer t = new Timer() {
            @Override
            public void run() {
                check(array[low], array.length, low, table, "number-check", increase);
                check(array[high], array.length, high, table, "number-check", increase);
                if (array.length == 0)
                    return;//завершить выполнение если длина массива равна 0
                if (low >= high)
                    return;//завершить выполнение если уже нечего делить

                // выбрать опорный элемент
                int middle = low + (high - low) / 2;

                int opora = array[middle];

                // разделить на подмассивы, который больше и меньше опорного элемента
                int i = low, j = high;
                while (i <= j) {
                    while (array[i] < opora) {
                        i++;
                    }

                    while (array[j] > opora) {
                        j--;
                    }
                    if (i <= j) {//меняем местами
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                        if (i == middle) {
                            check(array[i], array.length, i, table, "number-mid", increase);
                        } else {
                            check(array[i], array.length, i, table, "number-active", increase);
                        }
                        if (j == middle) {
                            check(array[j], array.length, j, table, "number-mid", increase);
                        } else {
                            check(array[j], array.length, j, table, "number-active", increase);
                        }

                        i++;
                        j--;
                    } else {
                        check(array[middle], array.length, middle, table, "number-mid", increase);
                    }
                }
                if (low < j)
                    draw(array, low, j, table, increase);

                if (high > i)
                    draw(array, i, high, table, increase);
                cancel();
            }

        };
        t.cancel();
        t.schedule(1000);
    }

    public static boolean validate(Button button, FlexTable table, String text) {
        boolean is30Here = false;
        if (Integer.parseInt(button.getText()) > 30) {

            button.addClickHandler(event -> {
                new MyPopup().show();
            });
        } else {
            is30Here = true;
            button.addClickHandler(event -> {
                table.removeAllRows();
                table.clear();
                int[] array = spawnArray(text, table);
                draw(array, 0, array.length - 1, table, true);
            });
        }
        return is30Here;
    }

    public static void check(int numberValue, int length, int i, FlexTable table, String styleName, boolean increase) {

        Button number = new Button(Integer.toString(numberValue));
        number.setStyleName(styleName);
        validate(number, table, Integer.toString(length));
        if (increase) {
            table.setWidget(i % 10, i / 10, number);
        } else {
            table.setWidget((length - i - 1) % 10, (length - i - 1) / 10, number);
        }
    }
}
