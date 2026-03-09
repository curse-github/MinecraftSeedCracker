/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UpgradeChunk
/*     */ {
/*     */   private int sides;
/*     */   private final ChunkPalettedStorageFix.Section[] sections;
/*     */   private final Dynamic<?> level;
/*     */   private final int x;
/*     */   private final int z;
/*     */   private final Int2ObjectMap<Dynamic<?>> blockEntities;
/*     */   
/*     */   public UpgradeChunk(Dynamic<?> level) {
/* 485 */     this.sections = new ChunkPalettedStorageFix.Section[16];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 490 */     this.blockEntities = new Int2ObjectLinkedOpenHashMap(16);
/*     */ 
/*     */     
/* 493 */     this.level = level;
/* 494 */     this.x = level.get("xPos").asInt(0) << 4;
/* 495 */     this.z = level.get("zPos").asInt(0) << 4;
/*     */     
/* 497 */     level.get("TileEntities").asStreamOpt().ifSuccess(s -> 
/* 498 */         s.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     boolean convertedFromAlphaFormat = level.get("convertedFromAlphaFormat").asBoolean(false);
/*     */     
/* 512 */     level.get("Sections").asStreamOpt().ifSuccess(s -> s.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 521 */       if (section != null)
/*     */       {
/*     */ 
/*     */         
/* 525 */         for (ObjectIterator objectIterator = section.toFix.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { IntListIterator intListIterator; Int2ObjectMap.Entry<IntList> entry = (Int2ObjectMap.Entry)objectIterator.next();
/* 526 */           int dy = section.y << 12;
/* 527 */           switch (entry.getIntKey()) {
/*     */             case 2:
/* 529 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 530 */                 pos |= dy;
/*     */                 
/* 532 */                 Dynamic<?> state = getBlock(pos);
/* 533 */                 if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(state))) {
/* 534 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 535 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 536 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_GRASS);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 3:
/* 543 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 544 */                 pos |= dy;
/*     */                 
/* 546 */                 Dynamic<?> state = getBlock(pos);
/* 547 */                 if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(state))) {
/* 548 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 549 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 550 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_PODZOL);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 110:
/* 557 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 558 */                 pos |= dy;
/*     */                 
/* 560 */                 Dynamic<?> state = getBlock(pos);
/* 561 */                 if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(state))) {
/* 562 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 563 */                   if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 564 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_MYCELIUM);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 25:
/* 571 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 572 */                 pos |= dy;
/* 573 */                 Dynamic<?> entity = removeBlockEntity(pos);
/* 574 */                 if (entity != null) {
/* 575 */                   String key = Boolean.toString(entity.get("powered").asBoolean(false)) + Boolean.toString(entity.get("powered").asBoolean(false));
/* 576 */                   setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.getOrDefault(key, (Dynamic)ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.get("false0")));
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 26:
/* 582 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 583 */                 pos |= dy;
/* 584 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 585 */                 Dynamic<?> state = getBlock(pos);
/* 586 */                 if (entity != null) {
/* 587 */                   int color = entity.get("color").asInt(0);
/* 588 */                   if (color != 14 && color >= 0 && color < 16) {
/* 589 */                     String key = ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "occupied") + ChunkPalettedStorageFix.getProperty(state, "part");
/* 590 */                     if (ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.containsKey(key)) {
/* 591 */                       setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.get(key));
/*     */                     }
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 176:
/*     */             case 177:
/* 600 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 601 */                 pos |= dy;
/* 602 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 603 */                 Dynamic<?> state = getBlock(pos);
/* 604 */                 if (entity != null) {
/* 605 */                   int color = entity.get("Base").asInt(0);
/* 606 */                   if (color != 15 && color >= 0 && color < 16) {
/* 607 */                     String key = ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing") + "_" + ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing");
/* 608 */                     if (ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.containsKey(key)) {
/* 609 */                       setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.get(key));
/*     */                     }
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 86:
/* 617 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 618 */                 pos |= dy;
/*     */                 
/* 620 */                 Dynamic<?> state = getBlock(pos);
/* 621 */                 if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(state))) {
/* 622 */                   String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN)));
/* 623 */                   if ("minecraft:grass_block".equals(name) || "minecraft:dirt".equals(name)) {
/* 624 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.PUMPKIN);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 140:
/* 631 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 632 */                 pos |= dy;
/* 633 */                 Dynamic<?> entity = removeBlockEntity(pos);
/* 634 */                 if (entity != null) {
/* 635 */                   String key = entity.get("Item").asString("") + entity.get("Item").asString("");
/* 636 */                   setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.getOrDefault(key, (Dynamic)ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.get("minecraft:air0")));
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 144:
/* 642 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 643 */                 pos |= dy;
/* 644 */                 Dynamic<?> entity = getBlockEntity(pos);
/* 645 */                 if (entity != null) {
/* 646 */                   String key, type = String.valueOf(entity.get("SkullType").asInt(0));
/* 647 */                   String facing = ChunkPalettedStorageFix.getProperty(getBlock(pos), "facing");
/*     */                   
/* 649 */                   if ("up".equals(facing) || "down".equals(facing)) {
/* 650 */                     key = type + type;
/*     */                   } else {
/* 652 */                     key = type + type;
/*     */                   } 
/*     */                   
/* 655 */                   entity.remove("SkullType");
/* 656 */                   entity.remove("facing");
/* 657 */                   entity.remove("Rot");
/*     */                   
/* 659 */                   setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.getOrDefault(key, (Dynamic)ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.get("0north")));
/*     */                 }  }
/*     */             
/*     */             
/*     */             case 64:
/*     */             case 71:
/*     */             case 193:
/*     */             case 194:
/*     */             case 195:
/*     */             case 196:
/*     */             case 197:
/* 670 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 671 */                 pos |= dy;
/*     */                 
/* 673 */                 Dynamic<?> state = getBlock(pos);
/* 674 */                 if (ChunkPalettedStorageFix.getName(state).endsWith("_door")) {
/* 675 */                   Dynamic<?> lower = getBlock(pos);
/* 676 */                   if ("lower".equals(ChunkPalettedStorageFix.getProperty(lower, "half"))) {
/* 677 */                     int abovePos = relative(pos, ChunkPalettedStorageFix.Direction.UP);
/* 678 */                     Dynamic<?> upper = getBlock(abovePos);
/* 679 */                     String name = ChunkPalettedStorageFix.getName(lower);
/* 680 */                     if (name.equals(ChunkPalettedStorageFix.getName(upper))) {
/* 681 */                       String facing = ChunkPalettedStorageFix.getProperty(lower, "facing");
/* 682 */                       String open = ChunkPalettedStorageFix.getProperty(lower, "open");
/* 683 */                       String hinge = convertedFromAlphaFormat ? "left" : ChunkPalettedStorageFix.getProperty(upper, "hinge");
/* 684 */                       String powered = convertedFromAlphaFormat ? "false" : ChunkPalettedStorageFix.getProperty(upper, "powered");
/* 685 */                       setBlock(pos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "lower" + facing + hinge + open));
/* 686 */                       setBlock(abovePos, (Dynamic)ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "upper" + facing + hinge + open));
/*     */                     } 
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 175:
/* 694 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = ((Integer)intListIterator.next()).intValue();
/* 695 */                 pos |= dy;
/*     */                 
/* 697 */                 Dynamic<?> block = getBlock(pos);
/* 698 */                 if ("upper".equals(ChunkPalettedStorageFix.getProperty(block, "half"))) {
/* 699 */                   Dynamic<?> below = getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN));
/* 700 */                   String variant = ChunkPalettedStorageFix.getName(below);
/* 701 */                   switch (variant) { case "minecraft:sunflower":
/* 702 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_SUNFLOWER);
/* 703 */                     case "minecraft:lilac": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LILAC);
/* 704 */                     case "minecraft:tall_grass": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_TALL_GRASS);
/* 705 */                     case "minecraft:large_fern": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LARGE_FERN);
/* 706 */                     case "minecraft:rose_bush": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_ROSE_BUSH);
/* 707 */                     case "minecraft:peony": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_PEONY); }
/*     */                 
/*     */                 }  }
/*     */             
/*     */           }  }
/*     */       
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 719 */   private Dynamic<?> getBlockEntity(int pos) { return (Dynamic)this.blockEntities.get(pos); }
/*     */ 
/*     */ 
/*     */   
/* 723 */   private Dynamic<?> removeBlockEntity(int pos) { return (Dynamic)this.blockEntities.remove(pos); }
/*     */   
/*     */   public static int relative(int pos, ChunkPalettedStorageFix.Direction direction) {
/*     */     int y, x;
/* 727 */     switch (direction.getAxis().ordinal()) { default: throw new MatchException(null, null);
/*     */       case 0:
/* 729 */         x = (pos & 0xF) + direction.getAxisDirection().getStep();
/* 730 */         return (x < 0 || x > 15) ? -1 : (pos & 0xFFFFFFF0 | x);
/*     */       
/*     */       case 1:
/* 733 */         y = (pos >> 8) + direction.getAxisDirection().getStep();
/* 734 */         return (y < 0 || y > 255) ? -1 : (pos & 0xFF | y << 8);
/*     */       case 2:
/*     */         break; }
/* 737 */      int z = (pos >> 4 & 0xF) + direction.getAxisDirection().getStep();
/* 738 */     return (z < 0 || z > 15) ? -1 : (pos & 0xFFFFFF0F | z << 4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setBlock(int pos, Dynamic<?> block) {
/* 744 */     if (pos < 0 || pos > 65535) {
/*     */       return;
/*     */     }
/*     */     
/* 748 */     ChunkPalettedStorageFix.Section section = getSection(pos);
/*     */     
/* 750 */     if (section == null) {
/*     */       return;
/*     */     }
/*     */     
/* 754 */     section.setBlock(pos & 0xFFF, block);
/*     */   }
/*     */   
/*     */   private ChunkPalettedStorageFix.Section getSection(int pos) {
/* 758 */     int sectionY = pos >> 12;
/* 759 */     return (sectionY < this.sections.length) ? this.sections[sectionY] : null;
/*     */   }
/*     */   
/*     */   public Dynamic<?> getBlock(int pos) {
/* 763 */     if (pos < 0 || pos > 65535) {
/* 764 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 767 */     ChunkPalettedStorageFix.Section section = getSection(pos);
/*     */     
/* 769 */     if (section == null) {
/* 770 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 773 */     return section.getBlock(pos & 0xFFF);
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 777 */     Dynamic<?> level = this.level;
/* 778 */     if (this.blockEntities.isEmpty()) {
/* 779 */       level = level.remove("TileEntities");
/*     */     } else {
/* 781 */       level = level.set("TileEntities", level.createList(this.blockEntities.values().stream()));
/*     */     } 
/*     */     
/* 784 */     Dynamic<?> indices = level.emptyMap();
/* 785 */     List<Dynamic<?>> sections = Lists.newArrayList();
/* 786 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 787 */       if (section != null) {
/* 788 */         sections.add(section.write());
/* 789 */         indices = indices.set(String.valueOf(section.y), indices.createIntList(Arrays.stream(section.update.toIntArray())));
/*     */       } 
/*     */     } 
/*     */     
/* 793 */     Dynamic<?> tag = level.emptyMap();
/* 794 */     tag = tag.set("Sides", tag.createByte((byte)this.sides));
/* 795 */     tag = tag.set("Indices", indices);
/* 796 */     return level.set("UpgradeData", tag).set("Sections", tag.createList(sections.stream()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$UpgradeChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */