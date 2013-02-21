package sk.kave.tank.events.mapchanged

import sk.kave.tank.beans.Items

/**
 * @author Igo
 */
class MapChangedEvent(val row: Int, val col: Int, val newValue: Items) {
}
