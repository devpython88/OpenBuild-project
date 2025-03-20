host_block = "Window"

local cycle = StateCycle.ready("Window", "_Window Open")

function interacted()
    cycle.cycle()
    local current = cycle.getCurrent()
    if (current == "Window") then
        playSound("Window Close")
    else
        playSound("Window Open")
    end
end

function update()
    setImageName(cycle.getCurrent())
end
