/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ 
/*     */ public class RailState
/*     */ {
/*     */   private final Level level;
/*     */   private final BlockPos pos;
/*     */   private final BaseRailBlock block;
/*     */   
/*     */   public RailState(Level level, BlockPos pos, BlockState state) {
/*  19 */     this.connections = Lists.newArrayList();
/*     */ 
/*     */     
/*  22 */     this.level = level;
/*  23 */     this.pos = pos;
/*  24 */     this.state = state;
/*  25 */     this.block = (BaseRailBlock)state.getBlock();
/*  26 */     RailShape direction = (RailShape)state.getValue(this.block.getShapeProperty());
/*  27 */     this.isStraight = this.block.isStraight();
/*  28 */     updateConnections(direction);
/*     */   }
/*     */   private BlockState state; private final boolean isStraight; private final List<BlockPos> connections;
/*     */   
/*  32 */   public List<BlockPos> getConnections() { return this.connections; }
/*     */ 
/*     */   
/*     */   private void updateConnections(RailShape direction) {
/*  36 */     this.connections.clear();
/*  37 */     switch (direction) {
/*     */       case NORTH_SOUTH:
/*  39 */         this.connections.add(this.pos.north());
/*  40 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case EAST_WEST:
/*  43 */         this.connections.add(this.pos.west());
/*  44 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_EAST:
/*  47 */         this.connections.add(this.pos.west());
/*  48 */         this.connections.add(this.pos.east().above());
/*     */         break;
/*     */       case ASCENDING_WEST:
/*  51 */         this.connections.add(this.pos.west().above());
/*  52 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_NORTH:
/*  55 */         this.connections.add(this.pos.north().above());
/*  56 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/*  59 */         this.connections.add(this.pos.north());
/*  60 */         this.connections.add(this.pos.south().above());
/*     */         break;
/*     */       case SOUTH_EAST:
/*  63 */         this.connections.add(this.pos.east());
/*  64 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case SOUTH_WEST:
/*  67 */         this.connections.add(this.pos.west());
/*  68 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case NORTH_WEST:
/*  71 */         this.connections.add(this.pos.west());
/*  72 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */       case NORTH_EAST:
/*  75 */         this.connections.add(this.pos.east());
/*  76 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeSoftConnections() {
/*  82 */     for (int i = 0; i < this.connections.size(); i++) {
/*  83 */       RailState rail = getRail((BlockPos)this.connections.get(i));
/*  84 */       if (rail == null || !rail.connectsTo(this)) {
/*  85 */         this.connections.remove(i--);
/*     */       } else {
/*  87 */         this.connections.set(i, rail.pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  93 */   private boolean hasRail(BlockPos pos) { return (BaseRailBlock.isRail(this.level, pos) || BaseRailBlock.isRail(this.level, pos.above()) || BaseRailBlock.isRail(this.level, pos.below())); }
/*     */ 
/*     */   
/*     */   private RailState getRail(BlockPos pos) {
/*  97 */     BlockPos testPos = pos;
/*  98 */     BlockState testState = this.level.getBlockState(testPos);
/*  99 */     if (BaseRailBlock.isRail(testState)) {
/* 100 */       return new RailState(this.level, testPos, testState);
/*     */     }
/*     */     
/* 103 */     testPos = pos.above();
/* 104 */     testState = this.level.getBlockState(testPos);
/* 105 */     if (BaseRailBlock.isRail(testState)) {
/* 106 */       return new RailState(this.level, testPos, testState);
/*     */     }
/*     */     
/* 109 */     testPos = pos.below();
/* 110 */     testState = this.level.getBlockState(testPos);
/* 111 */     if (BaseRailBlock.isRail(testState)) {
/* 112 */       return new RailState(this.level, testPos, testState);
/*     */     }
/*     */     
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 119 */   private boolean connectsTo(RailState rail) { return hasConnection(rail.pos); }
/*     */ 
/*     */   
/*     */   private boolean hasConnection(BlockPos railPos) {
/* 123 */     for (int i = 0; i < this.connections.size(); i++) {
/* 124 */       BlockPos pos = (BlockPos)this.connections.get(i);
/* 125 */       if (pos.getX() == railPos.getX() && pos.getZ() == railPos.getZ()) {
/* 126 */         return true;
/*     */       }
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   protected int countPotentialConnections() {
/* 133 */     int count = 0;
/*     */     
/* 135 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 136 */       if (hasRail(this.pos.relative(direction))) {
/* 137 */         count++;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return count;
/*     */   }
/*     */ 
/*     */   
/* 145 */   private boolean canConnectTo(RailState rail) { return (connectsTo(rail) || this.connections.size() != 2); }
/*     */ 
/*     */   
/*     */   private void connectTo(RailState rail) {
/* 149 */     this.connections.add(rail.pos);
/*     */     
/* 151 */     BlockPos north = this.pos.north();
/* 152 */     BlockPos south = this.pos.south();
/* 153 */     BlockPos west = this.pos.west();
/* 154 */     BlockPos east = this.pos.east();
/*     */     
/* 156 */     boolean n = hasConnection(north);
/* 157 */     boolean s = hasConnection(south);
/* 158 */     boolean w = hasConnection(west);
/* 159 */     boolean e = hasConnection(east);
/*     */     
/* 161 */     RailShape shape = null;
/*     */     
/* 163 */     if (n || s) {
/* 164 */       shape = RailShape.NORTH_SOUTH;
/*     */     }
/* 166 */     if (w || e) {
/* 167 */       shape = RailShape.EAST_WEST;
/*     */     }
/* 169 */     if (!this.isStraight) {
/* 170 */       if (s && e && !n && !w) {
/* 171 */         shape = RailShape.SOUTH_EAST;
/*     */       }
/* 173 */       if (s && w && !n && !e) {
/* 174 */         shape = RailShape.SOUTH_WEST;
/*     */       }
/* 176 */       if (n && w && !s && !e) {
/* 177 */         shape = RailShape.NORTH_WEST;
/*     */       }
/* 179 */       if (n && e && !s && !w) {
/* 180 */         shape = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 183 */     if (shape == RailShape.NORTH_SOUTH) {
/* 184 */       if (BaseRailBlock.isRail(this.level, north.above())) {
/* 185 */         shape = RailShape.ASCENDING_NORTH;
/*     */       }
/* 187 */       if (BaseRailBlock.isRail(this.level, south.above())) {
/* 188 */         shape = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 191 */     if (shape == RailShape.EAST_WEST) {
/* 192 */       if (BaseRailBlock.isRail(this.level, east.above())) {
/* 193 */         shape = RailShape.ASCENDING_EAST;
/*     */       }
/* 195 */       if (BaseRailBlock.isRail(this.level, west.above())) {
/* 196 */         shape = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 200 */     if (shape == null) {
/* 201 */       shape = RailShape.NORTH_SOUTH;
/*     */     }
/*     */     
/* 204 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), shape);
/* 205 */     this.level.setBlock(this.pos, this.state, 3);
/*     */   }
/*     */   
/*     */   private boolean hasNeighborRail(BlockPos railPos) {
/* 209 */     RailState neighbor = getRail(railPos);
/* 210 */     if (neighbor == null) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     neighbor.removeSoftConnections();
/* 215 */     return neighbor.canConnectTo(this);
/*     */   }
/*     */   
/*     */   public RailState place(boolean hasSignal, boolean first, RailShape defaultShape) {
/* 219 */     BlockPos north = this.pos.north();
/* 220 */     BlockPos south = this.pos.south();
/* 221 */     BlockPos west = this.pos.west();
/* 222 */     BlockPos east = this.pos.east();
/*     */     
/* 224 */     boolean n = hasNeighborRail(north);
/* 225 */     boolean s = hasNeighborRail(south);
/* 226 */     boolean w = hasNeighborRail(west);
/* 227 */     boolean e = hasNeighborRail(east);
/*     */     
/* 229 */     RailShape shape = null;
/*     */     
/* 231 */     boolean northOrSouth = (n || s);
/* 232 */     boolean westOrEast = (w || e);
/* 233 */     if (northOrSouth && !westOrEast) {
/* 234 */       shape = RailShape.NORTH_SOUTH;
/*     */     }
/* 236 */     if (westOrEast && !northOrSouth) {
/* 237 */       shape = RailShape.EAST_WEST;
/*     */     }
/*     */     
/* 240 */     boolean southAndEast = (s && e);
/* 241 */     boolean southAndWest = (s && w);
/* 242 */     boolean northAndEast = (n && e);
/* 243 */     boolean northAndWest = (n && w);
/*     */     
/* 245 */     if (!this.isStraight) {
/* 246 */       if (southAndEast && !n && !w) {
/* 247 */         shape = RailShape.SOUTH_EAST;
/*     */       }
/* 249 */       if (southAndWest && !n && !e) {
/* 250 */         shape = RailShape.SOUTH_WEST;
/*     */       }
/* 252 */       if (northAndWest && !s && !e) {
/* 253 */         shape = RailShape.NORTH_WEST;
/*     */       }
/* 255 */       if (northAndEast && !s && !w) {
/* 256 */         shape = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 259 */     if (shape == null) {
/* 260 */       if (northOrSouth && westOrEast) {
/* 261 */         shape = defaultShape;
/* 262 */       } else if (northOrSouth) {
/* 263 */         shape = RailShape.NORTH_SOUTH;
/* 264 */       } else if (westOrEast) {
/* 265 */         shape = RailShape.EAST_WEST;
/*     */       } 
/*     */       
/* 268 */       if (!this.isStraight) {
/* 269 */         if (hasSignal) {
/* 270 */           if (southAndEast) {
/* 271 */             shape = RailShape.SOUTH_EAST;
/*     */           }
/* 273 */           if (southAndWest) {
/* 274 */             shape = RailShape.SOUTH_WEST;
/*     */           }
/* 276 */           if (northAndEast) {
/* 277 */             shape = RailShape.NORTH_EAST;
/*     */           }
/* 279 */           if (northAndWest) {
/* 280 */             shape = RailShape.NORTH_WEST;
/*     */           }
/*     */         } else {
/* 283 */           if (northAndWest) {
/* 284 */             shape = RailShape.NORTH_WEST;
/*     */           }
/* 286 */           if (northAndEast) {
/* 287 */             shape = RailShape.NORTH_EAST;
/*     */           }
/* 289 */           if (southAndWest) {
/* 290 */             shape = RailShape.SOUTH_WEST;
/*     */           }
/* 292 */           if (southAndEast) {
/* 293 */             shape = RailShape.SOUTH_EAST;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 299 */     if (shape == RailShape.NORTH_SOUTH) {
/* 300 */       if (BaseRailBlock.isRail(this.level, north.above())) {
/* 301 */         shape = RailShape.ASCENDING_NORTH;
/*     */       }
/* 303 */       if (BaseRailBlock.isRail(this.level, south.above())) {
/* 304 */         shape = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 307 */     if (shape == RailShape.EAST_WEST) {
/* 308 */       if (BaseRailBlock.isRail(this.level, east.above())) {
/* 309 */         shape = RailShape.ASCENDING_EAST;
/*     */       }
/* 311 */       if (BaseRailBlock.isRail(this.level, west.above())) {
/* 312 */         shape = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 316 */     if (shape == null) {
/* 317 */       shape = defaultShape;
/*     */     }
/*     */     
/* 320 */     updateConnections(shape);
/* 321 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), shape);
/*     */     
/* 323 */     if (first || this.level.getBlockState(this.pos) != this.state) {
/* 324 */       this.level.setBlock(this.pos, this.state, 3);
/*     */       
/* 326 */       for (int i = 0; i < this.connections.size(); i++) {
/* 327 */         RailState neighbor = getRail((BlockPos)this.connections.get(i));
/* 328 */         if (neighbor != null) {
/*     */ 
/*     */           
/* 331 */           neighbor.removeSoftConnections();
/*     */           
/* 333 */           if (neighbor.canConnectTo(this)) {
/* 334 */             neighbor.connectTo(this);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 343 */   public BlockState getState() { return this.state; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RailState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */