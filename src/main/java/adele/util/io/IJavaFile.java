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
 *
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
package adele.util.io;

/*------------------------------\
| Liste des packages a importer |
\------------------------------*/
/*------------------------------------\
| Declaration de l'interface JavaFile |
\------------------------------------*/
/**
 *  Interface specification for java files
 *
 * @author    Remy Sanlaville
 * @since     21 fevrier 2003
 */
public interface IJavaFile
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)

  /*-------------------------------------\
  |      Methode(s) de l'interface       |
  \-------------------------------------*/
  /**
   *  Getter: Gets the name of the java file
   *
   * @return    The name value (example: "Server.java" or "Server.class")
   */
  public String getName();


  /**
   *  Getter: Gets the short name of the java file
   *
   * @return    The short name value (example: "Server")
   */
  public String getShortName();


  /**
   *  Getter:  Gets the suffix of the IJavaFile object
   *
   * @return    The suffix value (example: ".java" or ".class")
   */
  public String getSuffix();


  /**
   *  Getter:  Gets the qualify name of the java file
   *
   * @return    The qualify name value (example: "eo.test.Server")
   */
  public String getQualifyName();


  /**
   *  Getter:  Gets the full name of the java file
   *
   * @return    The full name value
   *                      (example: "P:\Appli1\source\Server.java"
   *                             or "P:\Appli1\classes\eo\test\Server.class")
   */
  public String getFullName();


  /**
   *  Getter: Gets the path of the java file
   *
   * @return    The path value (example: "P:\Appli1\classes\eo\test\")
   */
  public String getPath();


  /**
   *  Gets the archive of the java file
   *
   * @return    The archive value (example: "P:\Appli1\dist\appli1.jar").
   * Returns null if the java file is not included in an archive file.
   */
  public String getArchive();


  /**
   *  Gets the package of the java file
   *
   * @return    The package value (example: "eo.test")
   */
  public String getPackage();
}

