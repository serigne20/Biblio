#{{{ Marathon
require_fixture 'default'
#}}} Marathon

def test
        with_window("Bibliotheque") {
        select_fx_menu("#menu", "Edit>>Open File")
        select_file_chooser("#filechooser", "[\"#H/OneDrive/Bureau/Biblio.xml\"]")
        click("DBButton")
    }

    with_window("Synchronisation Base de données") {
        click("syncButton")
    }

end
