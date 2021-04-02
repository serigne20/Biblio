#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie la sauvegarde d'un fichier, en ouvrant un premier fichier, le modifié, le sauvegarder, rouvrir un second fichier et ouvrir de nouveau le 1er fichier normalement sauvegardé")
severity("normal")

def test
  randnum=rand(1..5)
    with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        click("tableBook", "{\"cell\":[2,\"Titre\"]}")
        click("ModifButton")
    }

    with_window("Modification d'un Livre") {
        select("ColonneInput", randnum.to_s)
        click("btvalider")
    }

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "File>>Save")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Test Etat.XML\"]")
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
    }


end
