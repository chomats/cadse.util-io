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
| Classe      : JavaSourceFile                                                |
| Type        : Model (MVC)                                                   |
| Etend       : JavaFile                                                      |
| Implemente  :                                                               |
| Description : modele pour les fichiers source de type Java.                 |
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

/*----------------------------------------\
| Declaration de la classe JavaSourceFile |
\----------------------------------------*/
/**
 *  Model for source code java files
 *
 * @author    Remy Sanlaville
 * @since     21 février 2003
 */
public class JavaSourceFile extends JavaFile
{
  // Constante(s) globale(s)
  /**  Description of the Field */
  final static String SUFFIX_SOURCE = "java";

  // Variable(s) globale(s)

  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /**
   *  Constructor : initialisation of the variables. We assume that the
   * arguments are corrects.
   *
   * @param  shortName    short name of the .class java file.
   *                              (example: "Server")
   * @param  path         path of the java source file
   *                              (example: "P:\Appli1\source\")
   * @param  archiveName  potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName  package structure of the java source file
   *                              (example: "eo.test")
   */
  public JavaSourceFile(String shortName, String path, String archiveName, String packageName)
  {
    init(shortName, path, archiveName, packageName, SUFFIX_SOURCE);
  }


  /**
   *  Constructor : initialisation of the variables. We assume that the
   * arguments are corrects.
   *
   * @param  fullName     full name of the java source file
   *                            (example: "P:\Appli1\source\Server.java")
   * @param  archiveName  potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName  package structure of the java source file
   *                            (example: "eo.test")
   */
  public JavaSourceFile(String fullName, String archiveName, String packageName)
  {
    init(fullName, archiveName, packageName);
  }
}

