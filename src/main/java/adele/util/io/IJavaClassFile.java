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
| Interface   : IJavaClassFile                                                |
| Type        : Model (MVC)                                                   |
| Etend       : IJavaFile                                                     |
| Implemente  :                                                               |
| Description : interface pour les fichiers java de type ".class".            |
| Version     : 1.0                                                           |
|-----------------------------------------------------------------------------|
| Teste sous  : JDK 1.4                                                       |
|-----------------------------------------------------------------------------|
| Auteur      : Remy Sanlaville                                               |
| Date        : 24/02/2003                                                    |
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

/*------------------------------------------\
| Declaration de l'interface IJavaClassFile |
\------------------------------------------*/
/**
 *  Interface specification for java files
 *
 * @author    Remy Sanlaville
 * @since     24 février 2003
 */
public interface IJavaClassFile extends IJavaFile
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)

  /*-------------------------------------\
  |      Methode(s) de l'interface       |
  \-------------------------------------*/
  /**
   *  Getter: Gets the classpath for the this class
   *
   * @return    The classpath value (example: "P:\Appli1\classes")
   */
  public String getClasspath();


  /**
   *  Introspection: Check if the class is a java interface
   *
   * @return    true if the class is a java interface, false otherwise
   */
  public boolean isInterface();
}

