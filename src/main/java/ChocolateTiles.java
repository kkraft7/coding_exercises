/*
For Guidewire coding test
*/
public class ChocolateTiles {
    public static int getRequestedTiles(int width, int height, int tiles) {
        if (tiles <= width*height && width > 0 && height > 0 && tiles > 0) {
            if (tiles % width == 0 || tiles % height == 0) {
                return 1;
            }
            if (tiles < width || tiles < height) {
                return 2;
            }
            if (tiles % 2 == 0) {

            }
        }
        return -1;
    }
}
