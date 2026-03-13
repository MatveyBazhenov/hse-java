package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainController {

    @FXML
    public ListView<String> left;
    @FXML
    public ListView<String> right;
    @FXML
    public Button move;
    @FXML
    public Button delete;
    @FXML
    public Button copy;

    private ListView<String> activePanel;

    private Path panelTasks(Path nameDir, ListView<String> panel) {
        int index = panel.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            File f = new File(nameDir.toFile().getAbsolutePath(), panel.getItems().get(index));
            if (f.isDirectory()) {
                nameDir = nameDir.resolve(panel.getItems().get(index));
                moveAction(nameDir, panel);
            }
        }
        if (index == 0) {
            nameDir = backAction(nameDir, panel);
        }
        return nameDir;
    }

    private void moveAction(Path nameDir, ListView<String> panel) {
        panel.getItems().clear();
        panel.getItems().add("...");
        for (File file : nameDir.toFile().listFiles()) {
            if (!file.isHidden()) {
                panel.getItems().add(file.getName());
            }
        }
    }

    private Path backAction(Path nameDir, ListView<String> panel) {
        System.out.println(nameDir.getParent());
        nameDir = nameDir.getParent();
        if (nameDir.equals(nameDir.getRoot())) {
            throw new RuntimeException("Выше директории нет");
        }
        moveAction(nameDir, panel);
        return nameDir;
    }

    void copy(Path nameDir, ListView<String> panel, Path toNameDir, ListView<String> toPanel) {
        int index = panel.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            File f = new File(nameDir.toFile().getAbsolutePath(), panel.getItems().get(index));
            if (f.isFile()) {
                if (f.isFile()) {
                    try {
                        Files.copy(nameDir.resolve(f.getName()), toNameDir.resolve(f.getName()));
                        moveAction(toNameDir, toPanel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    void move(Path nameDir, ListView<String> panel, Path toNameDir, ListView<String> toPanel) {
        int index = panel.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            File f = new File(nameDir.toFile().getAbsolutePath(), panel.getItems().get(index));
            if (f.isFile()) {
                if (f.isFile()) {
                    try {
                        Files.move(nameDir.resolve(f.getName()), toNameDir.resolve(f.getName()));
                        moveAction(toNameDir, toPanel);
                        moveAction(nameDir, panel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    void delete(Path nameDir, ListView<String> panel) {
        int index = panel.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            File f = new File(nameDir.toFile().getAbsolutePath(), panel.getItems().get(index));
            if (f.isFile()) {
                if (f.isFile()) {
                    try {
                        Files.delete(nameDir.resolve(f.getName()));
                        moveAction(nameDir, panel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Path leftDir;
    private Path rightDir;

    // for testing
    public void setInitialDirs(Path leftStart, Path rightStart) {
        this.leftDir = leftStart;
        this.rightDir = rightStart;
    }

    public void initialize() {
        leftDir = Path.of(System.getProperty("user.home"));
        rightDir = Path.of(System.getProperty("user.home"));
        System.out.println(leftDir.toFile().getAbsoluteFile());
        System.out.println(leftDir.toFile().getAbsolutePath());
        moveAction(leftDir, left);
        moveAction(rightDir, right);

        left.setOnMouseClicked(event -> {
            activePanel = left;
            if (event.getClickCount() == 2) {
                leftDir = panelTasks(leftDir, left);
            }
        });

        right.setOnMouseClicked(event -> {
            activePanel = right;
            if (event.getClickCount() == 2) {
                rightDir = panelTasks(rightDir, right);
            }
        });

        copy.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                if (activePanel == left) {
                    copy(leftDir, left, rightDir, right);
                }
                if (activePanel == right) {
                    copy(rightDir, right, leftDir, left);
                }
            }
        });

        move.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                if (activePanel == left) {
                    move(leftDir, left, rightDir, right);
                }
                if (activePanel == right) {
                    move(rightDir, right, leftDir, left);
                }
            }
        });

        delete.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                if (activePanel == left) {
                    delete(leftDir, left);
                }
                if (activePanel == right) {
                    delete(rightDir, right);
                }
            }
        });


    }
}
