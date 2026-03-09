/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MansionGrid
/*     */ {
/*     */   private static final int DEFAULT_SIZE = 11;
/*     */   private static final int CLEAR = 0;
/*     */   private static final int CORRIDOR = 1;
/*     */   private static final int ROOM = 2;
/*     */   private static final int START_ROOM = 3;
/*     */   private static final int TEST_ROOM = 4;
/*     */   private static final int BLOCKED = 5;
/*     */   private static final int ROOM_1x1 = 65536;
/*     */   private static final int ROOM_1x2 = 131072;
/*     */   private static final int ROOM_2x2 = 262144;
/*     */   private static final int ROOM_ORIGIN_FLAG = 1048576;
/*     */   private static final int ROOM_DOOR_FLAG = 2097152;
/*     */   private static final int ROOM_STAIRS_FLAG = 4194304;
/*     */   private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*     */   private static final int ROOM_TYPE_MASK = 983040;
/*     */   private static final int ROOM_ID_MASK = 65535;
/*     */   private final RandomSource random;
/*     */   private final WoodlandMansionPieces.SimpleGrid baseGrid;
/*     */   private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*     */   private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*     */   private final int entranceX;
/*     */   private final int entranceY;
/*     */   
/*     */   public MansionGrid(RandomSource random) {
/* 720 */     this.random = random;
/*     */     
/* 722 */     int houseSize = 11;
/* 723 */     this.entranceX = 7;
/* 724 */     this.entranceY = 4;
/*     */     
/* 726 */     this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 727 */     this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/* 728 */     this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/* 729 */     this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/* 730 */     this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/* 731 */     this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/* 732 */     this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/* 733 */     this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*     */     
/* 735 */     this.baseGrid.set(0, 0, 11, 1, 5);
/* 736 */     this.baseGrid.set(0, 9, 11, 11, 5);
/*     */     
/* 738 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/* 739 */     recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/* 740 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/* 741 */     recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/* 742 */     while (cleanEdges(this.baseGrid));
/*     */ 
/*     */     
/* 745 */     this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/* 746 */     this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 747 */     this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 748 */     this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/* 749 */     identifyRooms(this.baseGrid, this.floorRooms[0]);
/* 750 */     identifyRooms(this.baseGrid, this.floorRooms[1]);
/*     */ 
/*     */     
/* 753 */     this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/* 754 */     this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*     */     
/* 756 */     this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/* 757 */     setupThirdFloor();
/* 758 */     identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*     */   }
/*     */   
/*     */   public static boolean isHouse(WoodlandMansionPieces.SimpleGrid grid, int x, int y) {
/* 762 */     int value = grid.get(x, y);
/* 763 */     return (value == 1 || value == 2 || value == 3 || value == 4);
/*     */   }
/*     */ 
/*     */   
/* 767 */   public boolean isRoomId(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floor, int roomId) { return ((this.floorRooms[floor].get(x, y) & 0xFFFF) == roomId); }
/*     */ 
/*     */   
/*     */   public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floorNum, int roomId) {
/* 771 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 772 */       if (isRoomId(grid, x + direction.getStepX(), y + direction.getStepZ(), floorNum, roomId)) {
/* 773 */         return direction;
/*     */       }
/*     */     } 
/* 776 */     return null;
/*     */   }
/*     */   
/*     */   private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid grid, int x, int y, Direction heading, int depth) {
/* 780 */     if (depth <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 784 */     grid.set(x, y, 1);
/* 785 */     grid.setif(x + heading.getStepX(), y + heading.getStepZ(), 0, 1);
/*     */     
/* 787 */     for (int attempts = 0; attempts < 8; attempts++) {
/* 788 */       Direction nextDir = Direction.from2DDataValue(this.random.nextInt(4));
/* 789 */       if (nextDir != heading.getOpposite())
/*     */       {
/*     */         
/* 792 */         if (nextDir != Direction.EAST || !this.random.nextBoolean()) {
/*     */ 
/*     */ 
/*     */           
/* 796 */           int nx = x + heading.getStepX();
/* 797 */           int ny = y + heading.getStepZ();
/* 798 */           if (grid.get(nx + nextDir.getStepX(), ny + nextDir.getStepZ()) == 0 && grid.get(nx + nextDir.getStepX() * 2, ny + nextDir.getStepZ() * 2) == 0) {
/* 799 */             recursiveCorridor(grid, x + heading.getStepX() + nextDir.getStepX(), y + heading.getStepZ() + nextDir.getStepZ(), nextDir, depth - 1); break;
/*     */           } 
/*     */         }  } 
/*     */     } 
/* 803 */     Direction cw = heading.getClockWise();
/* 804 */     Direction ccw = heading.getCounterClockWise();
/* 805 */     grid.setif(x + cw.getStepX(), y + cw.getStepZ(), 0, 2);
/* 806 */     grid.setif(x + ccw.getStepX(), y + ccw.getStepZ(), 0, 2);
/*     */     
/* 808 */     grid.setif(x + heading.getStepX() + cw.getStepX(), y + heading.getStepZ() + cw.getStepZ(), 0, 2);
/* 809 */     grid.setif(x + heading.getStepX() + ccw.getStepX(), y + heading.getStepZ() + ccw.getStepZ(), 0, 2);
/* 810 */     grid.setif(x + heading.getStepX() * 2, y + heading.getStepZ() * 2, 0, 2);
/* 811 */     grid.setif(x + cw.getStepX() * 2, y + cw.getStepZ() * 2, 0, 2);
/* 812 */     grid.setif(x + ccw.getStepX() * 2, y + ccw.getStepZ() * 2, 0, 2);
/*     */   }
/*     */   
/*     */   private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid grid) {
/* 816 */     boolean touched = false;
/* 817 */     for (int y = 0; y < grid.height; y++) {
/* 818 */       for (int x = 0; x < grid.width; x++) {
/* 819 */         if (grid.get(x, y) == 0) {
/* 820 */           int directNeighbors = 0;
/* 821 */           directNeighbors += (isHouse(grid, x + 1, y) ? 1 : 0);
/* 822 */           directNeighbors += (isHouse(grid, x - 1, y) ? 1 : 0);
/* 823 */           directNeighbors += (isHouse(grid, x, y + 1) ? 1 : 0);
/* 824 */           directNeighbors += (isHouse(grid, x, y - 1) ? 1 : 0);
/*     */           
/* 826 */           if (directNeighbors >= 3) {
/*     */             
/* 828 */             grid.set(x, y, 2);
/* 829 */             touched = true;
/* 830 */           } else if (directNeighbors == 2) {
/*     */             
/* 832 */             int diagonalNeighbors = 0;
/* 833 */             diagonalNeighbors += (isHouse(grid, x + 1, y + 1) ? 1 : 0);
/* 834 */             diagonalNeighbors += (isHouse(grid, x - 1, y + 1) ? 1 : 0);
/* 835 */             diagonalNeighbors += (isHouse(grid, x + 1, y - 1) ? 1 : 0);
/* 836 */             diagonalNeighbors += (isHouse(grid, x - 1, y - 1) ? 1 : 0);
/* 837 */             if (diagonalNeighbors <= 1) {
/* 838 */               grid.set(x, y, 2);
/* 839 */               touched = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 845 */     return touched;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupThirdFloor() {
/* 850 */     List<Tuple<Integer, Integer>> potentialRooms = Lists.newArrayList();
/* 851 */     WoodlandMansionPieces.SimpleGrid floor = this.floorRooms[1];
/* 852 */     for (int y = 0; y < this.thirdFloorGrid.height; y++) {
/* 853 */       for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/* 854 */         int roomData = floor.get(x, y);
/* 855 */         int roomType = roomData & 0xF0000;
/* 856 */         if (roomType == 131072 && (roomData & 0x200000) == 2097152) {
/* 857 */           potentialRooms.add(new Tuple(Integer.valueOf(x), Integer.valueOf(y)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 862 */     if (potentialRooms.isEmpty()) {
/*     */       
/* 864 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*     */       
/*     */       return;
/*     */     } 
/* 868 */     Tuple<Integer, Integer> roomPos = (Tuple)potentialRooms.get(this.random.nextInt(potentialRooms.size()));
/* 869 */     int roomData = floor.get(((Integer)roomPos.getA()).intValue(), ((Integer)roomPos.getB()).intValue());
/* 870 */     floor.set(((Integer)roomPos.getA()).intValue(), ((Integer)roomPos.getB()).intValue(), roomData | 0x400000);
/* 871 */     Direction roomDir = get1x2RoomDirection(this.baseGrid, ((Integer)roomPos.getA()).intValue(), ((Integer)roomPos.getB()).intValue(), 1, roomData & 0xFFFF);
/* 872 */     int roomEndX = ((Integer)roomPos.getA()).intValue() + roomDir.getStepX();
/* 873 */     int roomEndY = ((Integer)roomPos.getB()).intValue() + roomDir.getStepZ();
/*     */     
/* 875 */     for (int y = 0; y < this.thirdFloorGrid.height; y++) {
/* 876 */       for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/* 877 */         if (!isHouse(this.baseGrid, x, y)) {
/* 878 */           this.thirdFloorGrid.set(x, y, 5);
/* 879 */         } else if (x == ((Integer)roomPos.getA()).intValue() && y == ((Integer)roomPos.getB()).intValue()) {
/* 880 */           this.thirdFloorGrid.set(x, y, 3);
/* 881 */         } else if (x == roomEndX && y == roomEndY) {
/* 882 */           this.thirdFloorGrid.set(x, y, 3);
/* 883 */           this.floorRooms[2].set(x, y, 8388608);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 888 */     List<Direction> potentialCorridors = Lists.newArrayList();
/* 889 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 890 */       if (this.thirdFloorGrid.get(roomEndX + direction.getStepX(), roomEndY + direction.getStepZ()) == 0) {
/* 891 */         potentialCorridors.add(direction);
/*     */       }
/*     */     } 
/*     */     
/* 895 */     if (potentialCorridors.isEmpty()) {
/*     */       
/* 897 */       this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/* 898 */       floor.set(((Integer)roomPos.getA()).intValue(), ((Integer)roomPos.getB()).intValue(), roomData);
/*     */       return;
/*     */     } 
/* 901 */     Direction corridorDir = (Direction)potentialCorridors.get(this.random.nextInt(potentialCorridors.size()));
/* 902 */     recursiveCorridor(this.thirdFloorGrid, roomEndX + corridorDir.getStepX(), roomEndY + corridorDir.getStepZ(), corridorDir, 4);
/* 903 */     while (cleanEdges(this.thirdFloorGrid));
/*     */   }
/*     */ 
/*     */   
/*     */   private void identifyRooms(WoodlandMansionPieces.SimpleGrid fromGrid, WoodlandMansionPieces.SimpleGrid roomGrid) {
/* 908 */     ObjectArrayList<Tuple<Integer, Integer>> roomPos = new ObjectArrayList<Tuple<Integer, Integer>>();
/* 909 */     for (int y = 0; y < fromGrid.height; y++) {
/* 910 */       for (int x = 0; x < fromGrid.width; x++) {
/* 911 */         if (fromGrid.get(x, y) == 2) {
/* 912 */           roomPos.add(new Tuple(Integer.valueOf(x), Integer.valueOf(y)));
/*     */         }
/*     */       } 
/*     */     } 
/* 916 */     Util.shuffle(roomPos, this.random);
/*     */     
/* 918 */     int roomId = 10;
/* 919 */     for (ObjectListIterator objectListIterator = roomPos.iterator(); objectListIterator.hasNext(); ) { Tuple<Integer, Integer> pos = (Tuple)objectListIterator.next();
/* 920 */       int x = ((Integer)pos.getA()).intValue();
/* 921 */       int y = ((Integer)pos.getB()).intValue();
/*     */       
/* 923 */       if (roomGrid.get(x, y) == 0) {
/* 924 */         int x0 = x;
/* 925 */         int x1 = x;
/* 926 */         int y0 = y;
/* 927 */         int y1 = y;
/* 928 */         int type = 65536;
/* 929 */         if (roomGrid.get(x + 1, y) == 0 && roomGrid.get(x, y + 1) == 0 && roomGrid.get(x + 1, y + 1) == 0 && fromGrid
/* 930 */           .get(x + 1, y) == 2 && fromGrid.get(x, y + 1) == 2 && fromGrid.get(x + 1, y + 1) == 2) {
/*     */           
/* 932 */           x1++;
/* 933 */           y1++;
/* 934 */           type = 262144;
/* 935 */         } else if (roomGrid.get(x - 1, y) == 0 && roomGrid.get(x, y + 1) == 0 && roomGrid.get(x - 1, y + 1) == 0 && fromGrid
/* 936 */           .get(x - 1, y) == 2 && fromGrid.get(x, y + 1) == 2 && fromGrid.get(x - 1, y + 1) == 2) {
/*     */           
/* 938 */           x0--;
/* 939 */           y1++;
/* 940 */           type = 262144;
/* 941 */         } else if (roomGrid.get(x - 1, y) == 0 && roomGrid.get(x, y - 1) == 0 && roomGrid.get(x - 1, y - 1) == 0 && fromGrid
/* 942 */           .get(x - 1, y) == 2 && fromGrid.get(x, y - 1) == 2 && fromGrid.get(x - 1, y - 1) == 2) {
/*     */           
/* 944 */           x0--;
/* 945 */           y0--;
/* 946 */           type = 262144;
/* 947 */         } else if (roomGrid.get(x + 1, y) == 0 && fromGrid.get(x + 1, y) == 2) {
/* 948 */           x1++;
/* 949 */           type = 131072;
/* 950 */         } else if (roomGrid.get(x, y + 1) == 0 && fromGrid.get(x, y + 1) == 2) {
/* 951 */           y1++;
/* 952 */           type = 131072;
/* 953 */         } else if (roomGrid.get(x - 1, y) == 0 && fromGrid.get(x - 1, y) == 2) {
/* 954 */           x0--;
/* 955 */           type = 131072;
/* 956 */         } else if (roomGrid.get(x, y - 1) == 0 && fromGrid.get(x, y - 1) == 2) {
/* 957 */           y0--;
/* 958 */           type = 131072;
/*     */         } 
/*     */ 
/*     */         
/* 962 */         int doorX = this.random.nextBoolean() ? x0 : x1;
/* 963 */         int doorY = this.random.nextBoolean() ? y0 : y1;
/* 964 */         int doorFlag = 2097152;
/* 965 */         if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 966 */           doorX = (doorX == x0) ? x1 : x0;
/* 967 */           doorY = (doorY == y0) ? y1 : y0;
/* 968 */           if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 969 */             doorY = (doorY == y0) ? y1 : y0;
/* 970 */             if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/* 971 */               doorX = (doorX == x0) ? x1 : x0;
/* 972 */               doorY = (doorY == y0) ? y1 : y0;
/* 973 */               if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*     */                 
/* 975 */                 doorFlag = 0;
/* 976 */                 doorX = x0;
/* 977 */                 doorY = y0;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 982 */         for (int ry = y0; ry <= y1; ry++) {
/* 983 */           for (int rx = x0; rx <= x1; rx++) {
/* 984 */             if (rx == doorX && ry == doorY) {
/* 985 */               roomGrid.set(rx, ry, 0x100000 | doorFlag | type | roomId);
/*     */             } else {
/* 987 */               roomGrid.set(rx, ry, type | roomId);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 992 */         roomId++;
/*     */       }  }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionGrid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */