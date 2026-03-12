/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Comparators;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NbtUtils
/*     */ {
/*  44 */   private static final Comparator<ListTag> YXZ_LISTTAG_INT_COMPARATOR = Comparator.comparingInt(list -> list.getIntOr(1, 0)).thenComparingInt(list -> list.getIntOr(0, 0)).thenComparingInt(list -> list.getIntOr(2, 0));
/*  45 */   private static final Comparator<ListTag> YXZ_LISTTAG_DOUBLE_COMPARATOR = Comparator.comparingDouble(list -> list.getDoubleOr(1, 0.0D)).thenComparingDouble(list -> list.getDoubleOr(0, 0.0D)).thenComparingDouble(list -> list.getDoubleOr(2, 0.0D));
/*     */   
/*  47 */   private static final Codec<ResourceKey<Block>> BLOCK_NAME_CODEC = ResourceKey.codec(Registries.BLOCK);
/*     */   
/*     */   public static final String SNBT_DATA_TAG = "data";
/*     */   
/*     */   private static final char PROPERTIES_START = '{';
/*     */   private static final char PROPERTIES_END = '}';
/*     */   private static final String ELEMENT_SEPARATOR = ",";
/*     */   private static final char KEY_VALUE_SEPARATOR = ':';
/*  55 */   private static final Splitter COMMA_SPLITTER = Splitter.on(",");
/*  56 */   private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);
/*     */   
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int INDENT = 2;
/*     */   
/*     */   private static final int NOT_FOUND = -1;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public static boolean compareNbt(Tag expected, Tag actual, boolean partialListMatches) {
/*  67 */     if (expected == actual) {
/*  68 */       return true;
/*     */     }
/*  70 */     if (expected == null) {
/*  71 */       return true;
/*     */     }
/*  73 */     if (actual == null) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!expected.getClass().equals(actual.getClass())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (expected instanceof CompoundTag) { CompoundTag expectedCompound = (CompoundTag)expected;
/*  81 */       CompoundTag actualCompound = (CompoundTag)actual;
/*     */       
/*  83 */       if (actualCompound.size() < expectedCompound.size()) {
/*  84 */         return false;
/*     */       }
/*     */       
/*  87 */       for (Map.Entry<String, Tag> entry : expectedCompound.entrySet()) {
/*  88 */         Tag tag = (Tag)entry.getValue();
/*  89 */         if (!compareNbt(tag, actualCompound.get((String)entry.getKey()), partialListMatches)) {
/*  90 */           return false;
/*     */         }
/*     */       } 
/*     */       
/*  94 */       return true; }
/*  95 */      if (expected instanceof ListTag) { ListTag expectedList = (ListTag)expected; if (partialListMatches) {
/*  96 */         ListTag actualList = (ListTag)actual;
/*     */         
/*  98 */         if (expectedList.isEmpty()) {
/*  99 */           return actualList.isEmpty();
/*     */         }
/*     */         
/* 102 */         if (actualList.size() < expectedList.size()) {
/* 103 */           return false;
/*     */         }
/*     */         
/* 106 */         for (Tag tag : expectedList) {
/* 107 */           boolean found = false;
/* 108 */           for (Tag value : actualList) {
/* 109 */             if (compareNbt(tag, value, partialListMatches)) {
/* 110 */               found = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 114 */           if (!found) {
/* 115 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 119 */         return true;
/*     */       }  }
/* 121 */      return expected.equals(actual);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockState readBlockState(HolderGetter<Block> blocks, CompoundTag tag) {
/* 126 */     Objects.requireNonNull(blocks); Optional<? extends Holder<Block>> blockHolder = tag.read("Name", BLOCK_NAME_CODEC).flatMap(blocks::get);
/* 127 */     if (blockHolder.isEmpty()) {
/* 128 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 131 */     Block block = (Block)((Holder)blockHolder.get()).value();
/* 132 */     BlockState result = block.defaultBlockState();
/*     */     
/* 134 */     Optional<CompoundTag> properties = tag.getCompound("Properties");
/* 135 */     if (properties.isPresent()) {
/* 136 */       StateDefinition<Block, BlockState> definition = block.getStateDefinition();
/* 137 */       for (String key : ((CompoundTag)properties.get()).keySet()) {
/* 138 */         Property<?> property = definition.getProperty(key);
/* 139 */         if (property != null) {
/* 140 */           result = (BlockState)setValueHelper(result, property, key, (CompoundTag)properties.get(), tag);
/*     */         }
/*     */       } 
/*     */     } 
/* 144 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S result, Property<T> property, String key, CompoundTag properties, CompoundTag tag) {
/* 149 */     Objects.requireNonNull(property); Optional<T> value = properties.getString(key).flatMap(property::getValue);
/* 150 */     if (value.isPresent()) {
/* 151 */       return (S)(StateHolder)result.setValue(property, (Comparable)value.get());
/*     */     }
/*     */     
/* 154 */     LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", new Object[] { key, properties.get(key), tag });
/* 155 */     return result;
/*     */   }
/*     */   
/*     */   public static CompoundTag writeBlockState(BlockState state) {
/* 159 */     CompoundTag tag = new CompoundTag();
/* 160 */     tag.putString("Name", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
/*     */     
/* 162 */     Map<Property<?>, Comparable<?>> values = state.getValues();
/* 163 */     if (!values.isEmpty()) {
/* 164 */       CompoundTag properties = new CompoundTag();
/*     */       
/* 166 */       for (Map.Entry<Property<?>, Comparable<?>> entry : values.entrySet()) {
/* 167 */         Property<?> key = (Property)entry.getKey();
/* 168 */         properties.putString(key.getName(), getName(key, (Comparable)entry.getValue()));
/*     */       } 
/* 170 */       tag.put("Properties", properties);
/*     */     } 
/*     */     
/* 173 */     return tag;
/*     */   }
/*     */   
/*     */   public static CompoundTag writeFluidState(FluidState state) {
/* 177 */     CompoundTag tag = new CompoundTag();
/* 178 */     tag.putString("Name", BuiltInRegistries.FLUID.getKey(state.getType()).toString());
/*     */     
/* 180 */     Map<Property<?>, Comparable<?>> values = state.getValues();
/* 181 */     if (!values.isEmpty()) {
/* 182 */       CompoundTag properties = new CompoundTag();
/*     */       
/* 184 */       for (Map.Entry<Property<?>, Comparable<?>> entry : values.entrySet()) {
/* 185 */         Property<?> key = (Property)entry.getKey();
/* 186 */         properties.putString(key.getName(), getName(key, (Comparable)entry.getValue()));
/*     */       } 
/* 188 */       tag.put("Properties", properties);
/*     */     } 
/*     */     
/* 191 */     return tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   private static <T extends Comparable<T>> String getName(Property<T> key, Comparable<?> value) { return key.getName(value); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static String prettyPrint(Tag tag) { return prettyPrint(tag, false); }
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static String prettyPrint(Tag tag, boolean withBinaryBlobs) { return prettyPrint(new StringBuilder(), tag, 0, withBinaryBlobs).toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder prettyPrint(StringBuilder builder, Tag input, int indent, boolean withBinaryBlobs) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore #4
/*     */     //   8: iconst_0
/*     */     //   9: istore #5
/*     */     //   11: aload #4
/*     */     //   13: iload #5
/*     */     //   15: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   20: tableswitch default -> 64, 0 -> 74, 1 -> 90, 2 -> 101, 3 -> 310, 4 -> 445, 5 -> 720, 6 -> 968
/*     */     //   64: new java/lang/MatchException
/*     */     //   67: dup
/*     */     //   68: aconst_null
/*     */     //   69: aconst_null
/*     */     //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   73: athrow
/*     */     //   74: aload #4
/*     */     //   76: checkcast net/minecraft/nbt/PrimitiveTag
/*     */     //   79: astore #6
/*     */     //   81: aload_0
/*     */     //   82: aload #6
/*     */     //   84: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   87: goto -> 1245
/*     */     //   90: aload #4
/*     */     //   92: checkcast net/minecraft/nbt/EndTag
/*     */     //   95: astore #7
/*     */     //   97: aload_0
/*     */     //   98: goto -> 1245
/*     */     //   101: aload #4
/*     */     //   103: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   106: astore #8
/*     */     //   108: aload #8
/*     */     //   110: invokevirtual getAsByteArray : ()[B
/*     */     //   113: astore #9
/*     */     //   115: aload #9
/*     */     //   117: arraylength
/*     */     //   118: istore #10
/*     */     //   120: iload_2
/*     */     //   121: aload_0
/*     */     //   122: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   125: ldc_w 'byte['
/*     */     //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   131: iload #10
/*     */     //   133: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   136: ldc_w '] {\\n'
/*     */     //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   142: pop
/*     */     //   143: iload_3
/*     */     //   144: ifeq -> 274
/*     */     //   147: iload_2
/*     */     //   148: iconst_1
/*     */     //   149: iadd
/*     */     //   150: aload_0
/*     */     //   151: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   154: pop
/*     */     //   155: iconst_0
/*     */     //   156: istore #11
/*     */     //   158: iload #11
/*     */     //   160: aload #9
/*     */     //   162: arraylength
/*     */     //   163: if_icmpge -> 271
/*     */     //   166: iload #11
/*     */     //   168: ifeq -> 178
/*     */     //   171: aload_0
/*     */     //   172: bipush #44
/*     */     //   174: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   177: pop
/*     */     //   178: iload #11
/*     */     //   180: bipush #16
/*     */     //   182: irem
/*     */     //   183: ifne -> 220
/*     */     //   186: iload #11
/*     */     //   188: bipush #16
/*     */     //   190: idiv
/*     */     //   191: ifle -> 220
/*     */     //   194: aload_0
/*     */     //   195: bipush #10
/*     */     //   197: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   200: pop
/*     */     //   201: iload #11
/*     */     //   203: aload #9
/*     */     //   205: arraylength
/*     */     //   206: if_icmpge -> 232
/*     */     //   209: iload_2
/*     */     //   210: iconst_1
/*     */     //   211: iadd
/*     */     //   212: aload_0
/*     */     //   213: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   216: pop
/*     */     //   217: goto -> 232
/*     */     //   220: iload #11
/*     */     //   222: ifeq -> 232
/*     */     //   225: aload_0
/*     */     //   226: bipush #32
/*     */     //   228: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   231: pop
/*     */     //   232: aload_0
/*     */     //   233: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   236: ldc_w '0x%02X'
/*     */     //   239: iconst_1
/*     */     //   240: anewarray java/lang/Object
/*     */     //   243: dup
/*     */     //   244: iconst_0
/*     */     //   245: aload #9
/*     */     //   247: iload #11
/*     */     //   249: baload
/*     */     //   250: sipush #255
/*     */     //   253: iand
/*     */     //   254: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   257: aastore
/*     */     //   258: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   264: pop
/*     */     //   265: iinc #11, 1
/*     */     //   268: goto -> 158
/*     */     //   271: goto -> 288
/*     */     //   274: iload_2
/*     */     //   275: iconst_1
/*     */     //   276: iadd
/*     */     //   277: aload_0
/*     */     //   278: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   281: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   284: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   287: pop
/*     */     //   288: aload_0
/*     */     //   289: bipush #10
/*     */     //   291: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   294: pop
/*     */     //   295: iload_2
/*     */     //   296: aload_0
/*     */     //   297: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   300: bipush #125
/*     */     //   302: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   305: pop
/*     */     //   306: aload_0
/*     */     //   307: goto -> 1245
/*     */     //   310: aload #4
/*     */     //   312: checkcast net/minecraft/nbt/ListTag
/*     */     //   315: astore #9
/*     */     //   317: aload #9
/*     */     //   319: invokevirtual size : ()I
/*     */     //   322: istore #10
/*     */     //   324: iload_2
/*     */     //   325: aload_0
/*     */     //   326: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   329: ldc_w 'list'
/*     */     //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   335: ldc_w '['
/*     */     //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   341: iload #10
/*     */     //   343: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   346: ldc_w '] ['
/*     */     //   349: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   352: pop
/*     */     //   353: iload #10
/*     */     //   355: ifeq -> 365
/*     */     //   358: aload_0
/*     */     //   359: bipush #10
/*     */     //   361: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   364: pop
/*     */     //   365: iconst_0
/*     */     //   366: istore #11
/*     */     //   368: iload #11
/*     */     //   370: iload #10
/*     */     //   372: if_icmpge -> 418
/*     */     //   375: iload #11
/*     */     //   377: ifeq -> 388
/*     */     //   380: aload_0
/*     */     //   381: ldc_w ',\\n'
/*     */     //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   387: pop
/*     */     //   388: iload_2
/*     */     //   389: iconst_1
/*     */     //   390: iadd
/*     */     //   391: aload_0
/*     */     //   392: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   395: pop
/*     */     //   396: aload_0
/*     */     //   397: aload #9
/*     */     //   399: iload #11
/*     */     //   401: invokevirtual get : (I)Lnet/minecraft/nbt/Tag;
/*     */     //   404: iload_2
/*     */     //   405: iconst_1
/*     */     //   406: iadd
/*     */     //   407: iload_3
/*     */     //   408: invokestatic prettyPrint : (Ljava/lang/StringBuilder;Lnet/minecraft/nbt/Tag;IZ)Ljava/lang/StringBuilder;
/*     */     //   411: pop
/*     */     //   412: iinc #11, 1
/*     */     //   415: goto -> 368
/*     */     //   418: iload #10
/*     */     //   420: ifeq -> 430
/*     */     //   423: aload_0
/*     */     //   424: bipush #10
/*     */     //   426: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   429: pop
/*     */     //   430: iload_2
/*     */     //   431: aload_0
/*     */     //   432: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   435: bipush #93
/*     */     //   437: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   440: pop
/*     */     //   441: aload_0
/*     */     //   442: goto -> 1245
/*     */     //   445: aload #4
/*     */     //   447: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   450: astore #10
/*     */     //   452: aload #10
/*     */     //   454: invokevirtual getAsIntArray : ()[I
/*     */     //   457: astore #11
/*     */     //   459: iconst_0
/*     */     //   460: istore #12
/*     */     //   462: aload #11
/*     */     //   464: astore #13
/*     */     //   466: aload #13
/*     */     //   468: arraylength
/*     */     //   469: istore #14
/*     */     //   471: iconst_0
/*     */     //   472: istore #15
/*     */     //   474: iload #15
/*     */     //   476: iload #14
/*     */     //   478: if_icmpge -> 525
/*     */     //   481: aload #13
/*     */     //   483: iload #15
/*     */     //   485: iaload
/*     */     //   486: istore #16
/*     */     //   488: iload #12
/*     */     //   490: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   493: ldc_w '%X'
/*     */     //   496: iconst_1
/*     */     //   497: anewarray java/lang/Object
/*     */     //   500: dup
/*     */     //   501: iconst_0
/*     */     //   502: iload #16
/*     */     //   504: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   507: aastore
/*     */     //   508: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   511: invokevirtual length : ()I
/*     */     //   514: invokestatic max : (II)I
/*     */     //   517: istore #12
/*     */     //   519: iinc #15, 1
/*     */     //   522: goto -> 474
/*     */     //   525: aload #11
/*     */     //   527: arraylength
/*     */     //   528: istore #13
/*     */     //   530: iload_2
/*     */     //   531: aload_0
/*     */     //   532: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   535: ldc_w 'int['
/*     */     //   538: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   541: iload #13
/*     */     //   543: invokevirtual append : (I)Ljava/lang/StringBuilder;
/*     */     //   546: ldc_w '] {\\n'
/*     */     //   549: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   552: pop
/*     */     //   553: iload_3
/*     */     //   554: ifeq -> 684
/*     */     //   557: iload_2
/*     */     //   558: iconst_1
/*     */     //   559: iadd
/*     */     //   560: aload_0
/*     */     //   561: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   564: pop
/*     */     //   565: iconst_0
/*     */     //   566: istore #14
/*     */     //   568: iload #14
/*     */     //   570: aload #11
/*     */     //   572: arraylength
/*     */     //   573: if_icmpge -> 681
/*     */     //   576: iload #14
/*     */     //   578: ifeq -> 588
/*     */     //   581: aload_0
/*     */     //   582: bipush #44
/*     */     //   584: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   587: pop
/*     */     //   588: iload #14
/*     */     //   590: bipush #16
/*     */     //   592: irem
/*     */     //   593: ifne -> 630
/*     */     //   596: iload #14
/*     */     //   598: bipush #16
/*     */     //   600: idiv
/*     */     //   601: ifle -> 630
/*     */     //   604: aload_0
/*     */     //   605: bipush #10
/*     */     //   607: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   610: pop
/*     */     //   611: iload #14
/*     */     //   613: aload #11
/*     */     //   615: arraylength
/*     */     //   616: if_icmpge -> 642
/*     */     //   619: iload_2
/*     */     //   620: iconst_1
/*     */     //   621: iadd
/*     */     //   622: aload_0
/*     */     //   623: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   626: pop
/*     */     //   627: goto -> 642
/*     */     //   630: iload #14
/*     */     //   632: ifeq -> 642
/*     */     //   635: aload_0
/*     */     //   636: bipush #32
/*     */     //   638: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   641: pop
/*     */     //   642: aload_0
/*     */     //   643: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   646: iload #12
/*     */     //   648: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   653: iconst_1
/*     */     //   654: anewarray java/lang/Object
/*     */     //   657: dup
/*     */     //   658: iconst_0
/*     */     //   659: aload #11
/*     */     //   661: iload #14
/*     */     //   663: iaload
/*     */     //   664: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   667: aastore
/*     */     //   668: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   671: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   674: pop
/*     */     //   675: iinc #14, 1
/*     */     //   678: goto -> 568
/*     */     //   681: goto -> 698
/*     */     //   684: iload_2
/*     */     //   685: iconst_1
/*     */     //   686: iadd
/*     */     //   687: aload_0
/*     */     //   688: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   691: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   694: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   697: pop
/*     */     //   698: aload_0
/*     */     //   699: bipush #10
/*     */     //   701: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   704: pop
/*     */     //   705: iload_2
/*     */     //   706: aload_0
/*     */     //   707: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   710: bipush #125
/*     */     //   712: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   715: pop
/*     */     //   716: aload_0
/*     */     //   717: goto -> 1245
/*     */     //   720: aload #4
/*     */     //   722: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   725: astore #11
/*     */     //   727: aload #11
/*     */     //   729: invokevirtual keySet : ()Ljava/util/Set;
/*     */     //   732: invokestatic newArrayList : (Ljava/lang/Iterable;)Ljava/util/ArrayList;
/*     */     //   735: astore #12
/*     */     //   737: aload #12
/*     */     //   739: invokestatic sort : (Ljava/util/List;)V
/*     */     //   742: iload_2
/*     */     //   743: aload_0
/*     */     //   744: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   747: bipush #123
/*     */     //   749: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   752: pop
/*     */     //   753: aload_0
/*     */     //   754: invokevirtual length : ()I
/*     */     //   757: aload_0
/*     */     //   758: ldc_w '\\n'
/*     */     //   761: invokevirtual lastIndexOf : (Ljava/lang/String;)I
/*     */     //   764: isub
/*     */     //   765: iconst_2
/*     */     //   766: iload_2
/*     */     //   767: iconst_1
/*     */     //   768: iadd
/*     */     //   769: imul
/*     */     //   770: if_icmple -> 788
/*     */     //   773: aload_0
/*     */     //   774: bipush #10
/*     */     //   776: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   779: pop
/*     */     //   780: iload_2
/*     */     //   781: iconst_1
/*     */     //   782: iadd
/*     */     //   783: aload_0
/*     */     //   784: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   787: pop
/*     */     //   788: aload #12
/*     */     //   790: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   795: <illegal opcode> applyAsInt : ()Ljava/util/function/ToIntFunction;
/*     */     //   800: invokeinterface mapToInt : (Ljava/util/function/ToIntFunction;)Ljava/util/stream/IntStream;
/*     */     //   805: invokeinterface max : ()Ljava/util/OptionalInt;
/*     */     //   810: iconst_0
/*     */     //   811: invokevirtual orElse : (I)I
/*     */     //   814: istore #13
/*     */     //   816: ldc_w ' '
/*     */     //   819: iload #13
/*     */     //   821: invokestatic repeat : (Ljava/lang/String;I)Ljava/lang/String;
/*     */     //   824: astore #14
/*     */     //   826: iconst_0
/*     */     //   827: istore #15
/*     */     //   829: iload #15
/*     */     //   831: aload #12
/*     */     //   833: invokeinterface size : ()I
/*     */     //   838: if_icmpge -> 936
/*     */     //   841: iload #15
/*     */     //   843: ifeq -> 854
/*     */     //   846: aload_0
/*     */     //   847: ldc_w ',\\n'
/*     */     //   850: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   853: pop
/*     */     //   854: aload #12
/*     */     //   856: iload #15
/*     */     //   858: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   863: checkcast java/lang/String
/*     */     //   866: astore #16
/*     */     //   868: iload_2
/*     */     //   869: iconst_1
/*     */     //   870: iadd
/*     */     //   871: aload_0
/*     */     //   872: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   875: bipush #34
/*     */     //   877: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   880: aload #16
/*     */     //   882: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   885: bipush #34
/*     */     //   887: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   890: aload #14
/*     */     //   892: iconst_0
/*     */     //   893: aload #14
/*     */     //   895: invokevirtual length : ()I
/*     */     //   898: aload #16
/*     */     //   900: invokevirtual length : ()I
/*     */     //   903: isub
/*     */     //   904: invokevirtual append : (Ljava/lang/CharSequence;II)Ljava/lang/StringBuilder;
/*     */     //   907: ldc_w ': '
/*     */     //   910: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   913: pop
/*     */     //   914: aload_0
/*     */     //   915: aload #11
/*     */     //   917: aload #16
/*     */     //   919: invokevirtual get : (Ljava/lang/String;)Lnet/minecraft/nbt/Tag;
/*     */     //   922: iload_2
/*     */     //   923: iconst_1
/*     */     //   924: iadd
/*     */     //   925: iload_3
/*     */     //   926: invokestatic prettyPrint : (Ljava/lang/StringBuilder;Lnet/minecraft/nbt/Tag;IZ)Ljava/lang/StringBuilder;
/*     */     //   929: pop
/*     */     //   930: iinc #15, 1
/*     */     //   933: goto -> 829
/*     */     //   936: aload #12
/*     */     //   938: invokeinterface isEmpty : ()Z
/*     */     //   943: ifne -> 953
/*     */     //   946: aload_0
/*     */     //   947: bipush #10
/*     */     //   949: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   952: pop
/*     */     //   953: iload_2
/*     */     //   954: aload_0
/*     */     //   955: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   958: bipush #125
/*     */     //   960: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   963: pop
/*     */     //   964: aload_0
/*     */     //   965: goto -> 1245
/*     */     //   968: aload #4
/*     */     //   970: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   973: astore #12
/*     */     //   975: aload #12
/*     */     //   977: invokevirtual getAsLongArray : ()[J
/*     */     //   980: astore #13
/*     */     //   982: lconst_0
/*     */     //   983: lstore #14
/*     */     //   985: aload #13
/*     */     //   987: astore #16
/*     */     //   989: aload #16
/*     */     //   991: arraylength
/*     */     //   992: istore #17
/*     */     //   994: iconst_0
/*     */     //   995: istore #18
/*     */     //   997: iload #18
/*     */     //   999: iload #17
/*     */     //   1001: if_icmpge -> 1049
/*     */     //   1004: aload #16
/*     */     //   1006: iload #18
/*     */     //   1008: laload
/*     */     //   1009: lstore #19
/*     */     //   1011: lload #14
/*     */     //   1013: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   1016: ldc_w '%X'
/*     */     //   1019: iconst_1
/*     */     //   1020: anewarray java/lang/Object
/*     */     //   1023: dup
/*     */     //   1024: iconst_0
/*     */     //   1025: lload #19
/*     */     //   1027: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */     //   1030: aastore
/*     */     //   1031: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1034: invokevirtual length : ()I
/*     */     //   1037: i2l
/*     */     //   1038: invokestatic max : (JJ)J
/*     */     //   1041: lstore #14
/*     */     //   1043: iinc #18, 1
/*     */     //   1046: goto -> 997
/*     */     //   1049: aload #13
/*     */     //   1051: arraylength
/*     */     //   1052: i2l
/*     */     //   1053: lstore #16
/*     */     //   1055: iload_2
/*     */     //   1056: aload_0
/*     */     //   1057: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1060: ldc_w 'long['
/*     */     //   1063: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1066: lload #16
/*     */     //   1068: invokevirtual append : (J)Ljava/lang/StringBuilder;
/*     */     //   1071: ldc_w '] {\\n'
/*     */     //   1074: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1077: pop
/*     */     //   1078: iload_3
/*     */     //   1079: ifeq -> 1209
/*     */     //   1082: iload_2
/*     */     //   1083: iconst_1
/*     */     //   1084: iadd
/*     */     //   1085: aload_0
/*     */     //   1086: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1089: pop
/*     */     //   1090: iconst_0
/*     */     //   1091: istore #18
/*     */     //   1093: iload #18
/*     */     //   1095: aload #13
/*     */     //   1097: arraylength
/*     */     //   1098: if_icmpge -> 1206
/*     */     //   1101: iload #18
/*     */     //   1103: ifeq -> 1113
/*     */     //   1106: aload_0
/*     */     //   1107: bipush #44
/*     */     //   1109: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1112: pop
/*     */     //   1113: iload #18
/*     */     //   1115: bipush #16
/*     */     //   1117: irem
/*     */     //   1118: ifne -> 1155
/*     */     //   1121: iload #18
/*     */     //   1123: bipush #16
/*     */     //   1125: idiv
/*     */     //   1126: ifle -> 1155
/*     */     //   1129: aload_0
/*     */     //   1130: bipush #10
/*     */     //   1132: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1135: pop
/*     */     //   1136: iload #18
/*     */     //   1138: aload #13
/*     */     //   1140: arraylength
/*     */     //   1141: if_icmpge -> 1167
/*     */     //   1144: iload_2
/*     */     //   1145: iconst_1
/*     */     //   1146: iadd
/*     */     //   1147: aload_0
/*     */     //   1148: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1151: pop
/*     */     //   1152: goto -> 1167
/*     */     //   1155: iload #18
/*     */     //   1157: ifeq -> 1167
/*     */     //   1160: aload_0
/*     */     //   1161: bipush #32
/*     */     //   1163: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1166: pop
/*     */     //   1167: aload_0
/*     */     //   1168: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
/*     */     //   1171: lload #14
/*     */     //   1173: <illegal opcode> makeConcatWithConstants : (J)Ljava/lang/String;
/*     */     //   1178: iconst_1
/*     */     //   1179: anewarray java/lang/Object
/*     */     //   1182: dup
/*     */     //   1183: iconst_0
/*     */     //   1184: aload #13
/*     */     //   1186: iload #18
/*     */     //   1188: laload
/*     */     //   1189: invokestatic valueOf : (J)Ljava/lang/Long;
/*     */     //   1192: aastore
/*     */     //   1193: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1199: pop
/*     */     //   1200: iinc #18, 1
/*     */     //   1203: goto -> 1093
/*     */     //   1206: goto -> 1223
/*     */     //   1209: iload_2
/*     */     //   1210: iconst_1
/*     */     //   1211: iadd
/*     */     //   1212: aload_0
/*     */     //   1213: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1216: ldc_w ' // Skipped, supply withBinaryBlobs true'
/*     */     //   1219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1222: pop
/*     */     //   1223: aload_0
/*     */     //   1224: bipush #10
/*     */     //   1226: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1229: pop
/*     */     //   1230: iload_2
/*     */     //   1231: aload_0
/*     */     //   1232: invokestatic indent : (ILjava/lang/StringBuilder;)Ljava/lang/StringBuilder;
/*     */     //   1235: bipush #125
/*     */     //   1237: invokevirtual append : (C)Ljava/lang/StringBuilder;
/*     */     //   1240: pop
/*     */     //   1241: aload_0
/*     */     //   1242: goto -> 1245
/*     */     //   1245: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #209	-> 0
/*     */     //   #210	-> 74
/*     */     //   #211	-> 90
/*     */     //   #212	-> 101
/*     */     //   #213	-> 108
/*     */     //   #215	-> 115
/*     */     //   #217	-> 120
/*     */     //   #218	-> 143
/*     */     //   #219	-> 147
/*     */     //   #220	-> 155
/*     */     //   #221	-> 166
/*     */     //   #222	-> 171
/*     */     //   #224	-> 178
/*     */     //   #225	-> 194
/*     */     //   #226	-> 201
/*     */     //   #227	-> 209
/*     */     //   #229	-> 220
/*     */     //   #230	-> 225
/*     */     //   #232	-> 232
/*     */     //   #220	-> 265
/*     */     //   #235	-> 274
/*     */     //   #237	-> 288
/*     */     //   #238	-> 295
/*     */     //   #239	-> 306
/*     */     //   #241	-> 310
/*     */     //   #242	-> 317
/*     */     //   #243	-> 324
/*     */     //   #244	-> 353
/*     */     //   #245	-> 358
/*     */     //   #248	-> 365
/*     */     //   #249	-> 375
/*     */     //   #250	-> 380
/*     */     //   #253	-> 388
/*     */     //   #254	-> 396
/*     */     //   #248	-> 412
/*     */     //   #256	-> 418
/*     */     //   #257	-> 423
/*     */     //   #259	-> 430
/*     */     //   #260	-> 441
/*     */     //   #262	-> 445
/*     */     //   #263	-> 452
/*     */     //   #265	-> 459
/*     */     //   #266	-> 462
/*     */     //   #267	-> 488
/*     */     //   #266	-> 519
/*     */     //   #270	-> 525
/*     */     //   #272	-> 530
/*     */     //   #274	-> 553
/*     */     //   #275	-> 557
/*     */     //   #276	-> 565
/*     */     //   #277	-> 576
/*     */     //   #278	-> 581
/*     */     //   #280	-> 588
/*     */     //   #281	-> 604
/*     */     //   #282	-> 611
/*     */     //   #283	-> 619
/*     */     //   #285	-> 630
/*     */     //   #286	-> 635
/*     */     //   #288	-> 642
/*     */     //   #276	-> 675
/*     */     //   #291	-> 684
/*     */     //   #294	-> 698
/*     */     //   #295	-> 705
/*     */     //   #296	-> 716
/*     */     //   #298	-> 720
/*     */     //   #299	-> 727
/*     */     //   #300	-> 737
/*     */     //   #302	-> 742
/*     */     //   #303	-> 753
/*     */     //   #304	-> 773
/*     */     //   #305	-> 780
/*     */     //   #308	-> 788
/*     */     //   #309	-> 816
/*     */     //   #311	-> 826
/*     */     //   #312	-> 841
/*     */     //   #313	-> 846
/*     */     //   #316	-> 854
/*     */     //   #317	-> 868
/*     */     //   #318	-> 914
/*     */     //   #311	-> 930
/*     */     //   #321	-> 936
/*     */     //   #322	-> 946
/*     */     //   #324	-> 953
/*     */     //   #325	-> 964
/*     */     //   #327	-> 968
/*     */     //   #328	-> 975
/*     */     //   #330	-> 982
/*     */     //   #331	-> 985
/*     */     //   #332	-> 1011
/*     */     //   #331	-> 1043
/*     */     //   #335	-> 1049
/*     */     //   #337	-> 1055
/*     */     //   #339	-> 1078
/*     */     //   #340	-> 1082
/*     */     //   #341	-> 1090
/*     */     //   #342	-> 1101
/*     */     //   #343	-> 1106
/*     */     //   #345	-> 1113
/*     */     //   #346	-> 1129
/*     */     //   #347	-> 1136
/*     */     //   #348	-> 1144
/*     */     //   #350	-> 1155
/*     */     //   #351	-> 1160
/*     */     //   #353	-> 1167
/*     */     //   #341	-> 1200
/*     */     //   #356	-> 1209
/*     */     //   #359	-> 1223
/*     */     //   #360	-> 1230
/*     */     //   #361	-> 1241
/*     */     //   #209	-> 1245
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   81	9	6	primitive	Lnet/minecraft/nbt/PrimitiveTag;
/*     */     //   97	4	7	ignored	Lnet/minecraft/nbt/EndTag;
/*     */     //   158	113	11	i	I
/*     */     //   115	195	9	array	[B
/*     */     //   120	190	10	length	I
/*     */     //   108	202	8	tag	Lnet/minecraft/nbt/ByteArrayTag;
/*     */     //   368	50	11	i	I
/*     */     //   324	121	10	size	I
/*     */     //   317	128	9	tag	Lnet/minecraft/nbt/ListTag;
/*     */     //   488	31	16	i	I
/*     */     //   568	113	14	i	I
/*     */     //   459	261	11	array	[I
/*     */     //   462	258	12	size	I
/*     */     //   530	190	13	length	I
/*     */     //   452	268	10	tag	Lnet/minecraft/nbt/IntArrayTag;
/*     */     //   868	62	16	key	Ljava/lang/String;
/*     */     //   829	107	15	i	I
/*     */     //   737	231	12	keys	Ljava/util/List;
/*     */     //   816	152	13	paddingLength	I
/*     */     //   826	142	14	padding	Ljava/lang/String;
/*     */     //   727	241	11	tag	Lnet/minecraft/nbt/CompoundTag;
/*     */     //   1011	32	19	i	J
/*     */     //   1093	113	18	i	I
/*     */     //   982	263	13	array	[J
/*     */     //   985	260	14	size	J
/*     */     //   1055	190	16	length	J
/*     */     //   975	270	12	tag	Lnet/minecraft/nbt/LongArrayTag;
/*     */     //   0	1246	0	builder	Ljava/lang/StringBuilder;
/*     */     //   0	1246	1	input	Lnet/minecraft/nbt/Tag;
/*     */     //   0	1246	2	indent	I
/*     */     //   0	1246	3	withBinaryBlobs	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   737	231	12	keys	Ljava/util/List<Ljava/lang/String;>; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder indent(int indent, StringBuilder builder) {
/* 367 */     int index = builder.lastIndexOf("\n") + 1;
/* 368 */     int len = builder.length() - index;
/*     */     
/* 370 */     for (int i = 0; i < 2 * indent - len; i++) {
/* 371 */       builder.append(' ');
/*     */     }
/* 373 */     return builder;
/*     */   }
/*     */ 
/*     */   
/* 377 */   public static Component toPrettyComponent(Tag tag) { return (new TextComponentTagVisitor("")).visit(tag); }
/*     */ 
/*     */ 
/*     */   
/* 381 */   public static String structureToSnbt(CompoundTag structure) { return (new SnbtPrinterTagVisitor()).visit(packStructureTemplate(structure)); }
/*     */ 
/*     */ 
/*     */   
/* 385 */   public static CompoundTag snbtToStructure(String snbt) throws CommandSyntaxException { return unpackStructureTemplate(TagParser.parseCompoundFully(snbt)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static CompoundTag packStructureTemplate(CompoundTag snbt) {
/*     */     ListTag palette;
/* 393 */     Optional<ListTag> palettes = snbt.getList("palettes");
/* 394 */     if (palettes.isPresent()) {
/* 395 */       palette = ((ListTag)palettes.get()).getListOrEmpty(0);
/*     */     } else {
/* 397 */       palette = snbt.getListOrEmpty("palette");
/*     */     } 
/*     */     
/* 400 */     ListTag deflatedPalette = (ListTag)palette.compoundStream().map(NbtUtils::packBlockState).map(StringTag::valueOf).collect(Collectors.toCollection(ListTag::new));
/*     */     
/* 402 */     snbt.put("palette", deflatedPalette);
/*     */ 
/*     */     
/* 405 */     if (palettes.isPresent()) {
/* 406 */       ListTag newPalettes = new ListTag();
/* 407 */       ((ListTag)palettes.get()).stream().flatMap(tag -> tag.asList().stream()).forEach(oldPalette -> {
/* 408 */             CompoundTag newPalette = new CompoundTag();
/* 409 */             for (int i = 0; i < oldPalette.size(); i++) {
/* 410 */               newPalette.putString((String)deflatedPalette.getString(i).orElseThrow(), packBlockState((CompoundTag)oldPalette.getCompound(i).orElseThrow()));
/*     */             }
/* 412 */             newPalettes.add(newPalette);
/*     */           });
/*     */       
/* 415 */       snbt.put("palettes", newPalettes);
/*     */     } 
/*     */ 
/*     */     
/* 419 */     Optional<ListTag> oldEntities = snbt.getList("entities");
/* 420 */     if (oldEntities.isPresent()) {
/*     */ 
/*     */       
/* 423 */       ListTag newEntities = (ListTag)((ListTag)oldEntities.get()).compoundStream().sorted(Comparator.comparing(tag -> tag.getList("pos"), Comparators.emptiesLast(YXZ_LISTTAG_DOUBLE_COMPARATOR))).collect(Collectors.toCollection(ListTag::new));
/* 424 */       snbt.put("entities", newEntities);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     ListTag blockData = (ListTag)snbt.getList("blocks").stream().flatMap(ListTag::compoundStream).sorted(Comparator.comparing(tag -> tag.getList("pos"), Comparators.emptiesLast(YXZ_LISTTAG_INT_COMPARATOR))).peek(block -> block.putString("state", (String)deflatedPalette.getString(block.getIntOr("state", 0)).orElseThrow())).collect(Collectors.toCollection(ListTag::new));
/*     */     
/* 433 */     snbt.put("data", blockData);
/* 434 */     snbt.remove("blocks");
/* 435 */     return snbt;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static CompoundTag unpackStructureTemplate(CompoundTag template) {
/* 441 */     ListTag packedPalette = template.getListOrEmpty("palette");
/*     */ 
/*     */     
/* 444 */     Map<String, Tag> palette = (Map)packedPalette.stream().flatMap(tag -> tag.asString().stream()).collect(ImmutableMap.toImmutableMap(Function.identity(), NbtUtils::unpackBlockState));
/*     */     
/* 446 */     Optional<ListTag> oldPalettes = template.getList("palettes");
/* 447 */     if (oldPalettes.isPresent()) {
/* 448 */       template.put("palettes", (Tag)((ListTag)oldPalettes.get()).compoundStream()
/* 449 */           .map(oldPalette -> 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 454 */             (ListTag)palette.keySet().stream().map(()).map(NbtUtils::unpackBlockState).collect(Collectors.toCollection(ListTag::new)))
/* 455 */           .collect(Collectors.toCollection(ListTag::new)));
/*     */       
/* 457 */       template.remove("palette");
/*     */     } else {
/* 459 */       template.put("palette", (Tag)palette.values().stream().collect(Collectors.toCollection(ListTag::new)));
/*     */     } 
/*     */     
/* 462 */     Optional<ListTag> maybeBlocks = template.getList("data");
/* 463 */     if (maybeBlocks.isPresent()) {
/* 464 */       Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 465 */       object2IntOpenHashMap.defaultReturnValue(-1);
/* 466 */       for (int i = 0; i < packedPalette.size(); i++) {
/* 467 */         object2IntOpenHashMap.put((String)packedPalette.getString(i).orElseThrow(), i);
/*     */       }
/*     */       
/* 470 */       ListTag blocks = (ListTag)maybeBlocks.get();
/* 471 */       for (int i = 0; i < blocks.size(); i++) {
/* 472 */         CompoundTag block = (CompoundTag)blocks.getCompound(i).orElseThrow();
/* 473 */         String stateName = (String)block.getString("state").orElseThrow();
/* 474 */         int stateId = object2IntOpenHashMap.getInt(stateName);
/* 475 */         if (stateId == -1) {
/* 476 */           throw new IllegalStateException("Entry " + stateName + " missing from palette");
/*     */         }
/* 478 */         block.putInt("state", stateId);
/*     */       } 
/*     */       
/* 481 */       template.put("blocks", blocks);
/* 482 */       template.remove("data");
/*     */     } 
/*     */     
/* 485 */     return template;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String packBlockState(CompoundTag compound) {
/* 490 */     StringBuilder builder = new StringBuilder((String)compound.getString("Name").orElseThrow());
/* 491 */     compound.getCompound("Properties").ifPresent(properties -> {
/*     */ 
/*     */ 
/*     */           
/* 495 */           String keyValues = (String)properties.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(()).collect(Collectors.joining(","));
/*     */           
/* 497 */           builder.append('{').append(keyValues).append('}');
/*     */         });
/* 499 */     return builder.toString();
/*     */   }
/*     */   @VisibleForTesting
/*     */   static CompoundTag unpackBlockState(String compound) throws CommandSyntaxException {
/*     */     String name;
/* 504 */     CompoundTag tag = new CompoundTag();
/* 505 */     int openIndex = compound.indexOf('{');
/*     */ 
/*     */     
/* 508 */     if (openIndex >= 0) {
/* 509 */       name = compound.substring(0, openIndex);
/* 510 */       CompoundTag properties = new CompoundTag();
/* 511 */       if (openIndex + 2 <= compound.length()) {
/* 512 */         String values = compound.substring(openIndex + 1, compound.indexOf('}', openIndex));
/* 513 */         COMMA_SPLITTER.split(values).forEach(keyValue -> {
/* 514 */               List<String> parts = COLON_SPLITTER.splitToList(keyValue);
/* 515 */               if (parts.size() == 2) {
/* 516 */                 properties.putString((String)parts.get(0), (String)parts.get(1));
/*     */               } else {
/* 518 */                 LOGGER.error("Something went wrong parsing: '{}' -- incorrect gamedata!", compound);
/*     */               } 
/*     */             });
/*     */         
/* 522 */         tag.put("Properties", properties);
/*     */       } 
/*     */     } else {
/* 525 */       name = compound;
/*     */     } 
/* 527 */     tag.putString("Name", name);
/* 528 */     return tag;
/*     */   }
/*     */   
/*     */   public static CompoundTag addCurrentDataVersion(CompoundTag tag) {
/* 532 */     int version = SharedConstants.getCurrentVersion().dataVersion().version();
/* 533 */     return addDataVersion(tag, version);
/*     */   }
/*     */   
/*     */   public static CompoundTag addDataVersion(CompoundTag tag, int version) {
/* 537 */     tag.putInt("DataVersion", version);
/* 538 */     return tag;
/*     */   }
/*     */   
/*     */   public static Dynamic<Tag> addCurrentDataVersion(Dynamic<Tag> tag) {
/* 542 */     int version = SharedConstants.getCurrentVersion().dataVersion().version();
/* 543 */     return addDataVersion(tag, version);
/*     */   }
/*     */ 
/*     */   
/* 547 */   public static Dynamic<Tag> addDataVersion(Dynamic<Tag> tag, int version) { return tag.set("DataVersion", tag.createInt(version)); }
/*     */ 
/*     */   
/*     */   public static void addCurrentDataVersion(ValueOutput output) {
/* 551 */     int version = SharedConstants.getCurrentVersion().dataVersion().version();
/* 552 */     addDataVersion(output, version);
/*     */   }
/*     */ 
/*     */   
/* 556 */   public static void addDataVersion(ValueOutput output, int version) { output.putInt("DataVersion", version); }
/*     */ 
/*     */ 
/*     */   
/* 560 */   public static int getDataVersion(CompoundTag tag) { return getDataVersion(tag, -1); }
/*     */ 
/*     */ 
/*     */   
/* 564 */   public static int getDataVersion(CompoundTag tag, int _default) { return tag.getIntOr("DataVersion", _default); }
/*     */ 
/*     */ 
/*     */   
/* 568 */   public static int getDataVersion(Dynamic<?> dynamic, int _default) { return dynamic.get("DataVersion").asInt(_default); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\NbtUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */