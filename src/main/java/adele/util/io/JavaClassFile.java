/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*----------------------------------------------------------------------------\
| Classe      : JavaClassFile                                                 |
| Type        : Model (MVC)                                                   |
| Etend       : JavaFile                                                      |
| Implemente  : IJavaClassFile                                                |
| Description : modele pour les fichiers java .class                          |
| Version     : 1.0                                                           |
|-----------------------------------------------------------------------------|
| Teste sous  : JDK 1.4                                                       |
|-----------------------------------------------------------------------------|
| Auteur      : Remy Sanlaville                                               |
| Date        : 21/02/2003                                                    |
|-----------------------------------------------------------------------------|
| Copyright   : LSR (Adele)                                                   |
\----------------------------------------------------------------------------*/
/*------------------------------\
| Definition du nom du package  |
\------------------------------*/
package adele.util.io;

/*------------------------------\
| Liste des packages a importer |
\------------------------------*/
//*** Packages de la librairie
//** Package util
//* Package I/O
import java.io.File;


/*---------------------------------------\
| Declaration de la classe JavaClassFile |
\---------------------------------------*/
/**
 *  Model for .class java files
 *
 * @author    Remy Sanlaville
 * @since     21 fevrier 2003
 */
public class JavaClassFile extends JavaFile implements IJavaClassFile
{
  // Constante(s) globale(s)
  /**  Description of the Field */
  final static String  SUFFIX_CLASS  = "class";

  // Variable(s) globale(s)
  /**  Description of the Field */
  private      String  classpath     = null;

  /**  Description of the Field */
  private      boolean typeInterface = false;



  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /**
   *  Constructor : initialisation of the variables. We assume that the
   * arguments are corrects.
   *
   * @param  shortName      short name of the .class java file.
   *                              (example: "Server")
   * @param  path           path of the .class java file
   *                              (example: "P:\Appli1\classes\eo\test")
   * @param  archiveName    potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName    package structure of the .class java file
   *                              (example: "eo.test")
   * @param  typeInterface  true if the class is an interface, false otherwise
   */
  public JavaClassFile(String shortName, String path, String archiveName, String packageName, boolean typeInterface)
  {
    init(shortName, path, archiveName, packageName, SUFFIX_CLASS);

    // Initialisation du classpath
    setClasspath();

    // Initialisation du type de la classe
    this.typeInterface = typeInterface;
  }


  /**
   *  Constructor : initialisation of the variables. We assume that the
   * arguments are corrects.
   *
   * @param  fullName       full name of the .class java file
   *                            (example: "P:\Appli1\classes\eo\test\Server.class")
   * @param  archiveName    potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName    package structure of the .class java file
   *                            (example: "eo.test")
   * @param  typeInterface  true if the class is an interface, false otherwise
   */
  public JavaClassFile(String fullName, String archiveName, String packageName, boolean typeInterface)
  {
    init(fullName, archiveName, packageName);

    // Initialisation du classpath
    setClasspath();

    // Initialisation du type de la classe
    this.typeInterface = typeInterface;
  }


  /*------------------------------------------------\
  |     Implementation de l'interface IJavaClass    |
  \------------------------------------------------*/
  /**
   *  Getter: Gets the classpath for the this java class file. We assume that
   * the file hierarchy is corresponding to the package structure.
   *
   * @return    The classpath value (example: "P:\Appli1\classes")
   */
  public String getClasspath()
  {
    return classpath;
  }


  /**
   *  Introspection: Check if the class is a java interface
   *
   * @return    true if the class is a java interface, false otherwise
   */
  public boolean isInterface()
  {
    return typeInterface;
  }


  /*--------------------------------------\
  |   Methode(s) Privee(s) de la classe   |
  \--------------------------------------*/
  /**  Setter : Sets the classpath value of the java class file */
  private void setClasspath()
  {
    // S'il existe un fichier archive, le classpath correspond a ce fichier
    // archive
    classpath = getArchive();

    // Sinon il faut calculer le classpath a partir du path et du nom de
    // package
    if (classpath == null)
      {
        // Recuperer le nom du package de la classe
        String packageName = getPackage();
        packageName = packageName.replace('.', File.separatorChar);

        // Recuperer le path
        classpath = getPath();
        if (classpath.endsWith(packageName))
          classpath = classpath.substring(0, classpath.length() - packageName.length() - 1);
      }
  }
}

