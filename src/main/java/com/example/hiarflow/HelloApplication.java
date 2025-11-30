package com.example.hiarflow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code depuis GitHub'
                git branch: 'main', url: 'https://github.com/affoul/devops_project.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Compilation du projet avec Maven'
                sh 'mvn clean install'
            }
        }

        stage('Tests') {
            steps {
                echo 'Aucun test JUnit pour l’instant'
                // Si tu ajoutes des tests plus tard, tu peux activer :
                // sh 'mvn test'
            }
        }

        stage('SAST') {
            steps {
                echo 'Analyse SonarQube (optionnel pour le mini-projet)'
                // sh 'mvn sonar:sonar -Dsonar.projectKey=devops_project ...'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Déploiement du projet (copie jar/war sur Tomcat)'
                // sh 'cp target/devops_project-1.0-SNAPSHOT.jar /chemin/tomcat/webapps/'
            }
        }
    }
}
HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}