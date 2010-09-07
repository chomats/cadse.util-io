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
//** Packages Util
//* Package I/O
import java.io.File;
import java.io.IOException;
import java.util.Random;


/*---------------------------------\
| Declaration de la classe TmpUtil |
\---------------------------------*/
/**
 *   Own Library for the manipulation of the tempory directories
 *
 * @author    Remy Sanlaville
 * @since     12 fevrier 2003
 */
public class TmpUtil
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)
  /**  Description of the Field */
  private File tmpDir = null;


  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /** Constructor: create a tempory directory */
  public TmpUtil()
  {
    // Construction d'un nouveau repertoire temporaire
    this(getNewTmpPath());
  }


  /**
   * Constructor: create a tempory directory denoted by the given pathname.
   *
   * @param  pathTmpDir  pathname of the tempory directory
   */
  public TmpUtil(String pathTmpDir)
  {
    // Creation du repertoire temporaire
    tmpDir = new File(pathTmpDir);

    if (!tmpDir.exists())
      // creation du repertoire temporaire
      tmpDir.mkdirs();
  }


  /*-------------------------------------------\
  |           Interface de la classe           |
  \-------------------------------------------*/
  /**
   *  Util : delete completely the current tempory directory
   *
   * @return    true if and only if the tempory directory is successfully
   * deleted; false otherwise
   */
  public boolean delete()
  {
    return FileUtil.deleteDir(tmpDir);
  }


  /**
   *  Util : gets the current tempory directory file
   *
   * @return    the current tempory directory file
   */
  public File getFile()
  {
    return tmpDir;
  }


  /**
   *  Util : gets the path of the current tempory directory
   *
   * @return    path of the current tempory directory
   */
  public String getPath()
  {
    try
      {
        return tmpDir.getCanonicalPath();
      }
    catch (IOException ioe)
      {
        ioe.printStackTrace();
      }
    return null;
  }


  /**
   *  Util : returns a free directory path name
   *
   * @return    free directory path name
   */
  public static String getNewTmpPath()
  {
    Random random  = new Random(System.currentTimeMillis());
    File   tempDir = null;
    int    unique  = 0;
    do
      {
        unique = random.nextInt() & Integer.MAX_VALUE;
        tempDir = new File(System.getProperty("java.io.tmpdir"), "TmpDir" + Integer.toString(unique));
      } while (tempDir.exists());

    // On a trouve un path !!
    try
      {
        return tempDir.getCanonicalPath();
      }
    catch (IOException ioe)
      {
        ioe.printStackTrace();
      }
    return null;
  }
}

