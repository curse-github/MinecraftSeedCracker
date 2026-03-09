/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static final abstract enum BlockFixers
/*     */   implements UpgradeData.BlockFixer
/*     */ {
/*     */   BLACKLIST, DEFAULT, CHEST, LEAVES, STEM_BLOCK;
/*     */   public static final Direction[] DIRECTIONS;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/chunk/UpgradeData$BlockFixers$1
/*     */     //   3: dup
/*     */     //   4: ldc 'BLACKLIST'
/*     */     //   6: iconst_0
/*     */     //   7: bipush #54
/*     */     //   9: anewarray net/minecraft/world/level/block/Block
/*     */     //   12: dup
/*     */     //   13: iconst_0
/*     */     //   14: getstatic net/minecraft/world/level/block/Blocks.OBSERVER : Lnet/minecraft/world/level/block/Block;
/*     */     //   17: aastore
/*     */     //   18: dup
/*     */     //   19: iconst_1
/*     */     //   20: getstatic net/minecraft/world/level/block/Blocks.NETHER_PORTAL : Lnet/minecraft/world/level/block/Block;
/*     */     //   23: aastore
/*     */     //   24: dup
/*     */     //   25: iconst_2
/*     */     //   26: getstatic net/minecraft/world/level/block/Blocks.WHITE_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   29: aastore
/*     */     //   30: dup
/*     */     //   31: iconst_3
/*     */     //   32: getstatic net/minecraft/world/level/block/Blocks.ORANGE_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   35: aastore
/*     */     //   36: dup
/*     */     //   37: iconst_4
/*     */     //   38: getstatic net/minecraft/world/level/block/Blocks.MAGENTA_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   41: aastore
/*     */     //   42: dup
/*     */     //   43: iconst_5
/*     */     //   44: getstatic net/minecraft/world/level/block/Blocks.LIGHT_BLUE_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   47: aastore
/*     */     //   48: dup
/*     */     //   49: bipush #6
/*     */     //   51: getstatic net/minecraft/world/level/block/Blocks.YELLOW_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   54: aastore
/*     */     //   55: dup
/*     */     //   56: bipush #7
/*     */     //   58: getstatic net/minecraft/world/level/block/Blocks.LIME_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   61: aastore
/*     */     //   62: dup
/*     */     //   63: bipush #8
/*     */     //   65: getstatic net/minecraft/world/level/block/Blocks.PINK_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   68: aastore
/*     */     //   69: dup
/*     */     //   70: bipush #9
/*     */     //   72: getstatic net/minecraft/world/level/block/Blocks.GRAY_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   75: aastore
/*     */     //   76: dup
/*     */     //   77: bipush #10
/*     */     //   79: getstatic net/minecraft/world/level/block/Blocks.LIGHT_GRAY_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   82: aastore
/*     */     //   83: dup
/*     */     //   84: bipush #11
/*     */     //   86: getstatic net/minecraft/world/level/block/Blocks.CYAN_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   89: aastore
/*     */     //   90: dup
/*     */     //   91: bipush #12
/*     */     //   93: getstatic net/minecraft/world/level/block/Blocks.PURPLE_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   96: aastore
/*     */     //   97: dup
/*     */     //   98: bipush #13
/*     */     //   100: getstatic net/minecraft/world/level/block/Blocks.BLUE_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   103: aastore
/*     */     //   104: dup
/*     */     //   105: bipush #14
/*     */     //   107: getstatic net/minecraft/world/level/block/Blocks.BROWN_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   110: aastore
/*     */     //   111: dup
/*     */     //   112: bipush #15
/*     */     //   114: getstatic net/minecraft/world/level/block/Blocks.GREEN_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   117: aastore
/*     */     //   118: dup
/*     */     //   119: bipush #16
/*     */     //   121: getstatic net/minecraft/world/level/block/Blocks.RED_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   124: aastore
/*     */     //   125: dup
/*     */     //   126: bipush #17
/*     */     //   128: getstatic net/minecraft/world/level/block/Blocks.BLACK_CONCRETE_POWDER : Lnet/minecraft/world/level/block/Block;
/*     */     //   131: aastore
/*     */     //   132: dup
/*     */     //   133: bipush #18
/*     */     //   135: getstatic net/minecraft/world/level/block/Blocks.ANVIL : Lnet/minecraft/world/level/block/Block;
/*     */     //   138: aastore
/*     */     //   139: dup
/*     */     //   140: bipush #19
/*     */     //   142: getstatic net/minecraft/world/level/block/Blocks.CHIPPED_ANVIL : Lnet/minecraft/world/level/block/Block;
/*     */     //   145: aastore
/*     */     //   146: dup
/*     */     //   147: bipush #20
/*     */     //   149: getstatic net/minecraft/world/level/block/Blocks.DAMAGED_ANVIL : Lnet/minecraft/world/level/block/Block;
/*     */     //   152: aastore
/*     */     //   153: dup
/*     */     //   154: bipush #21
/*     */     //   156: getstatic net/minecraft/world/level/block/Blocks.DRAGON_EGG : Lnet/minecraft/world/level/block/Block;
/*     */     //   159: aastore
/*     */     //   160: dup
/*     */     //   161: bipush #22
/*     */     //   163: getstatic net/minecraft/world/level/block/Blocks.GRAVEL : Lnet/minecraft/world/level/block/Block;
/*     */     //   166: aastore
/*     */     //   167: dup
/*     */     //   168: bipush #23
/*     */     //   170: getstatic net/minecraft/world/level/block/Blocks.SAND : Lnet/minecraft/world/level/block/Block;
/*     */     //   173: aastore
/*     */     //   174: dup
/*     */     //   175: bipush #24
/*     */     //   177: getstatic net/minecraft/world/level/block/Blocks.RED_SAND : Lnet/minecraft/world/level/block/Block;
/*     */     //   180: aastore
/*     */     //   181: dup
/*     */     //   182: bipush #25
/*     */     //   184: getstatic net/minecraft/world/level/block/Blocks.OAK_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   187: aastore
/*     */     //   188: dup
/*     */     //   189: bipush #26
/*     */     //   191: getstatic net/minecraft/world/level/block/Blocks.SPRUCE_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   194: aastore
/*     */     //   195: dup
/*     */     //   196: bipush #27
/*     */     //   198: getstatic net/minecraft/world/level/block/Blocks.BIRCH_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   201: aastore
/*     */     //   202: dup
/*     */     //   203: bipush #28
/*     */     //   205: getstatic net/minecraft/world/level/block/Blocks.ACACIA_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   208: aastore
/*     */     //   209: dup
/*     */     //   210: bipush #29
/*     */     //   212: getstatic net/minecraft/world/level/block/Blocks.CHERRY_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   215: aastore
/*     */     //   216: dup
/*     */     //   217: bipush #30
/*     */     //   219: getstatic net/minecraft/world/level/block/Blocks.JUNGLE_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   222: aastore
/*     */     //   223: dup
/*     */     //   224: bipush #31
/*     */     //   226: getstatic net/minecraft/world/level/block/Blocks.DARK_OAK_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   229: aastore
/*     */     //   230: dup
/*     */     //   231: bipush #32
/*     */     //   233: getstatic net/minecraft/world/level/block/Blocks.PALE_OAK_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   236: aastore
/*     */     //   237: dup
/*     */     //   238: bipush #33
/*     */     //   240: getstatic net/minecraft/world/level/block/Blocks.OAK_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   243: aastore
/*     */     //   244: dup
/*     */     //   245: bipush #34
/*     */     //   247: getstatic net/minecraft/world/level/block/Blocks.SPRUCE_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   250: aastore
/*     */     //   251: dup
/*     */     //   252: bipush #35
/*     */     //   254: getstatic net/minecraft/world/level/block/Blocks.BIRCH_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   257: aastore
/*     */     //   258: dup
/*     */     //   259: bipush #36
/*     */     //   261: getstatic net/minecraft/world/level/block/Blocks.ACACIA_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   264: aastore
/*     */     //   265: dup
/*     */     //   266: bipush #37
/*     */     //   268: getstatic net/minecraft/world/level/block/Blocks.JUNGLE_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   271: aastore
/*     */     //   272: dup
/*     */     //   273: bipush #38
/*     */     //   275: getstatic net/minecraft/world/level/block/Blocks.DARK_OAK_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   278: aastore
/*     */     //   279: dup
/*     */     //   280: bipush #39
/*     */     //   282: getstatic net/minecraft/world/level/block/Blocks.PALE_OAK_WALL_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   285: aastore
/*     */     //   286: dup
/*     */     //   287: bipush #40
/*     */     //   289: getstatic net/minecraft/world/level/block/Blocks.OAK_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   292: aastore
/*     */     //   293: dup
/*     */     //   294: bipush #41
/*     */     //   296: getstatic net/minecraft/world/level/block/Blocks.SPRUCE_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   299: aastore
/*     */     //   300: dup
/*     */     //   301: bipush #42
/*     */     //   303: getstatic net/minecraft/world/level/block/Blocks.BIRCH_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   306: aastore
/*     */     //   307: dup
/*     */     //   308: bipush #43
/*     */     //   310: getstatic net/minecraft/world/level/block/Blocks.ACACIA_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   313: aastore
/*     */     //   314: dup
/*     */     //   315: bipush #44
/*     */     //   317: getstatic net/minecraft/world/level/block/Blocks.JUNGLE_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   320: aastore
/*     */     //   321: dup
/*     */     //   322: bipush #45
/*     */     //   324: getstatic net/minecraft/world/level/block/Blocks.DARK_OAK_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   327: aastore
/*     */     //   328: dup
/*     */     //   329: bipush #46
/*     */     //   331: getstatic net/minecraft/world/level/block/Blocks.PALE_OAK_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   334: aastore
/*     */     //   335: dup
/*     */     //   336: bipush #47
/*     */     //   338: getstatic net/minecraft/world/level/block/Blocks.OAK_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   341: aastore
/*     */     //   342: dup
/*     */     //   343: bipush #48
/*     */     //   345: getstatic net/minecraft/world/level/block/Blocks.SPRUCE_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   348: aastore
/*     */     //   349: dup
/*     */     //   350: bipush #49
/*     */     //   352: getstatic net/minecraft/world/level/block/Blocks.BIRCH_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   355: aastore
/*     */     //   356: dup
/*     */     //   357: bipush #50
/*     */     //   359: getstatic net/minecraft/world/level/block/Blocks.ACACIA_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   362: aastore
/*     */     //   363: dup
/*     */     //   364: bipush #51
/*     */     //   366: getstatic net/minecraft/world/level/block/Blocks.JUNGLE_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   369: aastore
/*     */     //   370: dup
/*     */     //   371: bipush #52
/*     */     //   373: getstatic net/minecraft/world/level/block/Blocks.DARK_OAK_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   376: aastore
/*     */     //   377: dup
/*     */     //   378: bipush #53
/*     */     //   380: getstatic net/minecraft/world/level/block/Blocks.PALE_OAK_WALL_HANGING_SIGN : Lnet/minecraft/world/level/block/Block;
/*     */     //   383: aastore
/*     */     //   384: invokespecial <init> : (Ljava/lang/String;I[Lnet/minecraft/world/level/block/Block;)V
/*     */     //   387: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.BLACKLIST : Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   390: new net/minecraft/world/level/chunk/UpgradeData$BlockFixers$2
/*     */     //   393: dup
/*     */     //   394: ldc 'DEFAULT'
/*     */     //   396: iconst_1
/*     */     //   397: iconst_0
/*     */     //   398: anewarray net/minecraft/world/level/block/Block
/*     */     //   401: invokespecial <init> : (Ljava/lang/String;I[Lnet/minecraft/world/level/block/Block;)V
/*     */     //   404: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.DEFAULT : Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   407: new net/minecraft/world/level/chunk/UpgradeData$BlockFixers$3
/*     */     //   410: dup
/*     */     //   411: ldc 'CHEST'
/*     */     //   413: iconst_2
/*     */     //   414: iconst_2
/*     */     //   415: anewarray net/minecraft/world/level/block/Block
/*     */     //   418: dup
/*     */     //   419: iconst_0
/*     */     //   420: getstatic net/minecraft/world/level/block/Blocks.CHEST : Lnet/minecraft/world/level/block/Block;
/*     */     //   423: aastore
/*     */     //   424: dup
/*     */     //   425: iconst_1
/*     */     //   426: getstatic net/minecraft/world/level/block/Blocks.TRAPPED_CHEST : Lnet/minecraft/world/level/block/Block;
/*     */     //   429: aastore
/*     */     //   430: invokespecial <init> : (Ljava/lang/String;I[Lnet/minecraft/world/level/block/Block;)V
/*     */     //   433: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.CHEST : Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   436: new net/minecraft/world/level/chunk/UpgradeData$BlockFixers$4
/*     */     //   439: dup
/*     */     //   440: ldc 'LEAVES'
/*     */     //   442: iconst_3
/*     */     //   443: iconst_1
/*     */     //   444: bipush #8
/*     */     //   446: anewarray net/minecraft/world/level/block/Block
/*     */     //   449: dup
/*     */     //   450: iconst_0
/*     */     //   451: getstatic net/minecraft/world/level/block/Blocks.ACACIA_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   454: aastore
/*     */     //   455: dup
/*     */     //   456: iconst_1
/*     */     //   457: getstatic net/minecraft/world/level/block/Blocks.CHERRY_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   460: aastore
/*     */     //   461: dup
/*     */     //   462: iconst_2
/*     */     //   463: getstatic net/minecraft/world/level/block/Blocks.BIRCH_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   466: aastore
/*     */     //   467: dup
/*     */     //   468: iconst_3
/*     */     //   469: getstatic net/minecraft/world/level/block/Blocks.PALE_OAK_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   472: aastore
/*     */     //   473: dup
/*     */     //   474: iconst_4
/*     */     //   475: getstatic net/minecraft/world/level/block/Blocks.DARK_OAK_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   478: aastore
/*     */     //   479: dup
/*     */     //   480: iconst_5
/*     */     //   481: getstatic net/minecraft/world/level/block/Blocks.JUNGLE_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   484: aastore
/*     */     //   485: dup
/*     */     //   486: bipush #6
/*     */     //   488: getstatic net/minecraft/world/level/block/Blocks.OAK_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   491: aastore
/*     */     //   492: dup
/*     */     //   493: bipush #7
/*     */     //   495: getstatic net/minecraft/world/level/block/Blocks.SPRUCE_LEAVES : Lnet/minecraft/world/level/block/Block;
/*     */     //   498: aastore
/*     */     //   499: invokespecial <init> : (Ljava/lang/String;IZ[Lnet/minecraft/world/level/block/Block;)V
/*     */     //   502: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.LEAVES : Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   505: new net/minecraft/world/level/chunk/UpgradeData$BlockFixers$5
/*     */     //   508: dup
/*     */     //   509: ldc_w 'STEM_BLOCK'
/*     */     //   512: iconst_4
/*     */     //   513: iconst_2
/*     */     //   514: anewarray net/minecraft/world/level/block/Block
/*     */     //   517: dup
/*     */     //   518: iconst_0
/*     */     //   519: getstatic net/minecraft/world/level/block/Blocks.MELON_STEM : Lnet/minecraft/world/level/block/Block;
/*     */     //   522: aastore
/*     */     //   523: dup
/*     */     //   524: iconst_1
/*     */     //   525: getstatic net/minecraft/world/level/block/Blocks.PUMPKIN_STEM : Lnet/minecraft/world/level/block/Block;
/*     */     //   528: aastore
/*     */     //   529: invokespecial <init> : (Ljava/lang/String;I[Lnet/minecraft/world/level/block/Block;)V
/*     */     //   532: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.STEM_BLOCK : Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   535: invokestatic $values : ()[Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   538: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.$VALUES : [Lnet/minecraft/world/level/chunk/UpgradeData$BlockFixers;
/*     */     //   541: invokestatic values : ()[Lnet/minecraft/core/Direction;
/*     */     //   544: putstatic net/minecraft/world/level/chunk/UpgradeData$BlockFixers.DIRECTIONS : [Lnet/minecraft/core/Direction;
/*     */     //   547: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #228	-> 0
/*     */     //   #291	-> 390
/*     */     //   #297	-> 407
/*     */     //   #324	-> 436
/*     */     //   #377	-> 505
/*     */     //   #227	-> 535
/*     */     //   #391	-> 541
/*     */   }
/*     */   
/*     */   BlockFixers(boolean chunky, Block... blocks) {
/* 398 */     for (Block block : blocks) {
/* 399 */       UpgradeData.MAP.put(block, this);
/*     */     }
/* 401 */     if (chunky)
/* 402 */       UpgradeData.CHUNKY_FIXERS.add(this); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */