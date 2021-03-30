#{{{ Marathon
require_fixture 'default'
#}}} Marathon

description("Test AboutUS ")
severity("normal")
def test
      with_window("Bibliotheque") {
        select_fx_menu("#menu", "About>>About us")
        window_closed("Trombinoscope")
    }

end
