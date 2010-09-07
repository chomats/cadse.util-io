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
//*** Packages de la librairie
//** Package util
//* Package I/O
import java.io.File;

/*----------------------------------\
| Declaration de la classe JavaFile |
\----------------------------------*/
/**
 *  Abstract class model for java files
 *
 * @author    Remy Sanlaville
 * @since     21 fevrier 2003
 */
public abstract class JavaFile implements IJavaFile
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)
  /**  short name of the java file (example: "Server"). */
  private String shortName   = null;
  /**  path of the java file (example: "P:\Appli1\source"). */
  private String path        = null;
  /**
   * potential archive incluing the java file. Can be null
   * (example: "P:\Appli1\dist\appli1.jar").
   */
  private String archiveName = null;
  /**  package structure of the java file (example: "eo.test"). */
  private String packageName = null;

  /**
   *  name of the suffix for the java file (example: "java" or "class").
   *  Must be initialised by subclass.
   */
  private String suffix      = null;


  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /**
   *  Init : initialisation of the attributes.
   *
   * @param  shortName    short name of the java file
   *                              (example: "Server")
   * @param  path         path of the java file
   *                              (example: "P:\Appli1\source\")
   * @param  archiveName  potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName  package structure of the java file
   *                              (example: "eo.test")
   * @param  suffix       name of the suffix for the java file
   *                              (example "java" or "class")
   */
  protected void init(String shortName, String path, String archiveName, String packageName, String suffix)
  {
    // Tester si nous sommes dans le cas d'une archive
    if ((archiveName != null) && (!archiveName.equals("")))
      {
        // Re-initialiser les separateurs des repertoires
        // On est oblige de faire cela car les fichier zip on toujours
        // des separateurs de repertoires '/'
        path = path.replace('/', File.separatorChar);
      }

    // Initialise the attributes
    setShortName(shortName);
    setPath(path);
    setArchive(archiveName);
    setPackage(packageName);
    setSuffix(suffix);
  }


  /**
   *  Init : initialisation of the attributes.
   *
   * @param  name         name of the java file (example: Server.java
   *                                                   or Server.class)
   * @param  path         path of the java file (example: "P:\Appli1\source\").
   * The path must ended with a File.separator.
   * @param  archiveName  potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName  package structure of the java file (example: "eo.test")
   */
  protected void init(String name, String path, String archiveName, String packageName)
  {
    // Tester si nous sommes dans le cas d'une archive
    if ((archiveName != null) && (!archiveName.equals("")))
      {
        // Re-initialiser les separateurs des repertoires
        // On est oblige de faire cela car les fichier zip on toujours
        // des separateurs de repertoires '/'
        name = name.replace('/', File.separatorChar);
      }

    // Recuperer le nom court et le suffixe du nom du fichier
    String tmpShortName = FileUtil.getShortName(name);
    String tmpSuffix    = FileUtil.getExtension(name);

    init(tmpShortName, path, archiveName, packageName, tmpSuffix);
  }


  /**
   *  Init : initialisation of the attributes.
   *
   * @param  fullName     full name of the java file
   *                      (example: "P:\Appli1\source\Server.java"
   *                             or "P:\Appli1\classes\eo\test\Server.class")
   * @param  archiveName  potential archive name incluing the java file.
   *                             (example: "P:\Appli1\dist\appli1.jar" or null)
   * @param  packageName  package structure of the java file (example: "eo.test")
   */
  protected void init(String fullName, String archiveName, String packageName)
  {
    // Tester si nous sommes dans le cas d'une archive
    if ((archiveName != null) && (!archiveName.equals("")))
      {
        // Re-initialiser les separateurs des repertoires
        // On est oblige de faire cela car les fichier zip on toujours
        // des separateurs de repertoires '/'
        fullName = fullName.replace('/', File.separatorChar);
      }

    // Decouper fullName en path - name
    // Example: "P:\Appli1\source\Server.java"
    //       -> "P:\Appli1\source\" - "Server.java"
    String tmpName = FileUtil.getName(fullName);
    String tmpPath = FileUtil.getPath(fullName);

    init(tmpName, tmpPath, archiveName, packageName);
  }


  /*------------------------------------------------\
  |     Implementation de l'interface IJavaFile     |
  \------------------------------------------------*/
  /**
   *  Getter: Gets the name of the java file
   *
   * @return    The name value (example: "Server.java" or "Server.class")
   */
  public String getName()
  {
    return getShortName() + "." + getSuffix();
  }


  /**
   *  Getter: Gets the short name of the java file
   *
   * @return    The short name value (example: "Server")
   */
  public String getShortName()
  {
    return shortName;
  }


  /**
   *  Getter: Gets the suffix of the java file
   *
   * @return    The suffix value (example: "java" or "class")
   */
  public String getSuffix()
  {
    return suffix;
  }


  /**
   *  Getter:  Gets the qualify name of the java file
   *
   * @return    The qualify name value (example: "eo.test.Server")
   */
  public String getQualifyName()
  {
    return getPackage() + "." + getShortName();
  }


  /**
   *  Getter: Gets the full name of the java file
   *
   * @return    The full name value<br>
   *           (example: "P:\Appli1\source\Server.java" or "P:\Appli1\classes\eo\test\Server.class")<br>
   */
  public String getFullName()
  {
    return getPath() + File.separator + getName();
  }


  /**
   *  Gets the path of the java file
   *
   * @return    The path value (example: "P:\Appli1\source\")
   */
  public String getPath()
  {
    return path;
  }


  /**
   *  Gets the archive of the java file
   *
   * @return    The archive value (example: "P:\Appli1\dist\appli1.jar").
   * Returns null if the java file is not included in an archive file.
   */
  public String getArchive()
  {
    return archiveName;
  }


  /**
   *  Gets the package of the java file
   *
   * @return    The package value (example: "control")
   */
  public String getPackage()
  {
    return packageName;
  }


  /*----------------------------------------------\
  |    *** Methodes protected de la classe ***    |
  \----------------------------------------------*/
  /**
   *  Setter: Sets the short name of the java file
   *
   * @param  shortName  The new short name value (example: "Server")
   */
  protected void setShortName(String shortName)
  {
    // Test if the shortName is null
    if (shortName == null)
      shortName = "";

    this.shortName = shortName;
  }


  /**
   *  Setter: Sets the suffix of the java file
   *
   * @param  suffix  The new suffix value (example: "java" or "class")
   */
  protected void setSuffix(String suffix)
  {
    // Test if the suffix is null
    if (suffix == null)
      suffix = "";

    this.suffix = suffix;
  }


  /**
   *  Setter: Sets the path of the java file
   *
   * @param  path  The new path value (example: "P:\Appli1\eo\test\")
   */
  protected void setPath(String path)
  {
    // Test if the path is null
    if (path == null)
      path = "";
    else
      {
        // Test if path ends with a file separator
        if (path.endsWith(File.separator))
          // Delete this ended separator
          path = path.substring(0, path.length() - 1);
      }

    this.path = path;
  }


  /**
   *  Setter: Sets the potential archive file incluing the java file
   *
   * @param  archiveName  The new archive value (example: "P:\Appli1\dist\appli1.jar")
   */
  protected void setArchive(String archiveName)
  {
    // Mise a jour du nom de l'archive
    this.archiveName = archiveName;
  }


  /**
   *  Setter: Sets the package of the java file
   *
   * @param  packageName  The new pachage value (example: "eo.test")
   */
  protected void setPackage(String packageName)
  {
    // Test if the packageName is null
    if (packageName == null)
      packageName = "";
    else
      {
        // Test if packageName ends with a "."
        if (packageName.endsWith("."))
          packageName = packageName.substring(0, packageName.length() - 1);
      }

    this.packageName = packageName;
  }
}

