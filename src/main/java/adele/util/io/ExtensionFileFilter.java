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
//** Packages util
//* Package I/O
import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/*---------------------------------------------\
| Declaration de la classe ExtensionFileFilter |
\---------------------------------------------*/
/**
 *  Description of the Class
 *
 * @author    Remy Sanlaville
 * @since     20 aout 2002
 */
public class ExtensionFileFilter extends FileFilter
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)
  /**  Description of the Field */
  private ArrayList extensionFilters           = null;
  /**  Description of the Field */
  private String    descriptionFilter          = null;
  /**  Description of the Field */
  private String    fullDescription            = null;
  /**  Description of the Field */
  private boolean   useExtensionsInDescription = true;


  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /**
   * Constructor: Creates a file filter that accepts the given file type.
   * Example: new ExtensionFileFilter("jpg", "JPEG Image");
   *
   * Note that the "." before the extension is not needed. If
   * provided, it will be ignored.
   *
   * @param  extension    Description of Parameter
   * @param  description  Description of Parameter
   * @see                 #addExtension
   */
  public ExtensionFileFilter(String extension, String description)
  {
    // Ajout de l'extension et du descripteur passes en argument
    addExtension(extension);
    setDescription(description);
  }


  /**
   * Constructor: Creates a file filter from the given string array and
   * description. Example: new ExtensionFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
   *
   * Note that the "." before the extension is not needed. If
   * provided, it will be ignored.
   *
   * @param  filters      Description of Parameter
   * @param  description  Description of Parameter
   * @see                 #addExtension
   */
  public ExtensionFileFilter(String[] filters, String description)
  {
    // Ajout des l'extensions les uns apres les autres
    if (filters != null)
      {
        for (int i = 0; i < filters.length; i++)
          {
            addExtension(filters[i]);
          }
      }

    // Ajout du descripteur passe en argument
    setDescription(description);
  }


  /*-----------------------------------------------------------------\
  |      Implementation de l'interface pour le JFileChooser          |
  \-----------------------------------------------------------------*/
  /**
   * Accepte tous les repertoires et les fichiers dont l'extension est identique a la propriete extensionFilter de la classe
   *
   * @param  f  le fichier a tester
   * @return    true si le fichier est un repertoire ou si il a la meme extension que la propriete extensionFilter
   */
  public boolean accept(File f)
  {
    // Accepter le fichier si c'est un repertoire
    if (f.isDirectory())
      return true;

    String extension = FileUtil.getExtension(f);
    if ((extensionFilters == null) || (extensionFilters.isEmpty()))
      {
        if (extension == null)
          return true;
        else
          return false;
      }

    if (extension == null)
      return false;
    else
      return extensionFilters.contains(extension);
  }


  /**
   * Adds an extension filter.
   *
   * For example: filter.addExtension("jpg");
   *
   * Note that the "." before the extension is not needed. If
   * provided, it will be ignored.
   *
   * @param  extension  the extension filter to add
   */
  public void addExtension(String extension)
  {
    // Verifions que l'extension passe en argument ne soit pas null !
    if (extension != null)
      {
        // Verifier que le vecteur des extensions n'est pas null
        if (extensionFilters == null)
          {
            // Creation du vecteur des extensions
            extensionFilters = new ArrayList();
          }

        // Ajout de l'extension passe en argument dans le vecteur des extensions
        // Ne prendre que l'extension et pas le '.' ("jpg" mais pas ".jpg")
        int i = extension.lastIndexOf('.');
        if (i > 0 && i < extension.length() - 1)
          extensionFilters.add(extension.substring(i + 1).toLowerCase());
        else
          extensionFilters.add(extension.toLowerCase());
      }
  }


  /**
   * Setter: Sets the human readable description of this filter. For
   * example: filter.setDescription("Gif and JPG Images");
   *
   * @param  description  The new description value
   */
  public void setDescription(String description)
  {
    // Verifions que la description passee en argument ne soit pas null !
    if (description != null)
      descriptionFilter = description;
  }


  /**
   * Introspecteur : Returns the human readable description of this filter defined
   * by setDescription method.
   *
   * @return    the human readable description of this filter
   * @see       #setDescription
   */
  public String getDescription()
  {
    return descriptionFilter;
  }
}


