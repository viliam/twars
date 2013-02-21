package sk.kave.tank.events.mapchanged

import sk.kave.tank.beans.Items

/**
 * @author Igo
 */
class MapChangedEvent(val col: Int, val row: Int, val newValue: Items) {
}