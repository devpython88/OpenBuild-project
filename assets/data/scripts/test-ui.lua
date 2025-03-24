local button


-- Make sure you create the button IN the `scriptLoaded` function
function scriptLoaded()
    button = UI.TextView.ready("Hello!", 100, 100)
    button.setFontSize(10)
end
