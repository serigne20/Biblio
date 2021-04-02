#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Verifie si l'app ouvre le trombinoscope")
severity("normal")

def test

    with_window("Bibliotheque") {
        select_fx_menu("#menu", "About>>About us")
        window_closed("Trombinoscope")
    }

end
