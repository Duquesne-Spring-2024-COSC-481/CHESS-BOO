public class Move {
  private int startRow;
  private int startCol;
  private int endRow;
  private int endCol;

  public Move(int startRow, int startCol, int endRow, int endCol) {
      this.startRow = startRow;
      this.startCol = startCol;
      this.endRow = endRow;
      this.endCol = endCol;
  }

  public int getStartRow() {
      return startRow;
  }

  public int getStartCol() {
      return startCol;
  }

  public int getEndRow() {
      return endRow;
  }

  public int getEndCol() {
      return endCol;
  }

  @Override
  public String toString() {
      return String.format("(%d, %d) -> (%d, %d)", startRow, startCol, endRow, endCol);
  }

  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Move other = (Move) obj;
      return startRow == other.startRow &&
             startCol == other.startCol &&
             endRow == other.endRow &&
             endCol == other.endCol;
  }

  @Override
  public int hashCode() {
      int result = 17;
      result = 31 * result + startRow;
      result = 31 * result + startCol;
      result = 31 * result + endRow;
      result = 31 * result + endCol;
      return result;
  }
}
