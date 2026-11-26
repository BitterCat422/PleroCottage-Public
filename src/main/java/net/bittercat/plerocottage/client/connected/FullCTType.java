package net.bittercat.plerocottage.client.connected;

final class FullCTType {
    static final int SHEET_SIZE = 8;

    private FullCTType() {
    }

    static int getTextureIndex(boolean up, boolean down, boolean left, boolean right,
                               boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        int x = 0, y = 0;
        int borders = (!up ? 1 : 0) + (!down ? 1 : 0) + (!left ? 1 : 0) + (!right ? 1 : 0);
        if (up) x++;
        if (down) x += 2;
        if (left) y++;
        if (right) y += 2;
        if (borders == 0) {
            if (topRight) x++;
            if (topLeft) x += 2;
            if (bottomRight) y += 2;
            if (bottomLeft) y++;
        }
        if (borders == 1) {
            if (!right && (topLeft || bottomLeft)) {
                y = 4;
                x = -1 + (bottomLeft ? 1 : 0) + (topLeft ? 2 : 0);
            }
            if (!left && (topRight || bottomRight)) {
                y = 5;
                x = -1 + (bottomRight ? 1 : 0) + (topRight ? 2 : 0);
            }
            if (!down && (topLeft || topRight)) {
                y = 6;
                x = -1 + (topLeft ? 1 : 0) + (topRight ? 2 : 0);
            }
            if (!up && (bottomLeft || bottomRight)) {
                y = 7;
                x = -1 + (bottomLeft ? 1 : 0) + (bottomRight ? 2 : 0);
            }
        }
        if (borders == 2 && ((up && left && topLeft) || (down && left && bottomLeft)
            || (up && right && topRight) || (down && right && bottomRight))) {
            x += 3;
        }
        return x + 8 * y;
    }
}
