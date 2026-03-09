/*     */ package net.minecraft.commands.arguments;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public class NbtPathArgument extends Object implements ArgumentType<NbtPathArgument.NbtPath> {
/*  36 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}" });
/*  37 */   public static final SimpleCommandExceptionType ERROR_INVALID_NODE = new SimpleCommandExceptionType(Component.translatable("arguments.nbtpath.node.invalid"));
/*  38 */   public static final SimpleCommandExceptionType ERROR_DATA_TOO_DEEP = new SimpleCommandExceptionType(Component.translatable("arguments.nbtpath.too_deep"));
/*  39 */   public static final DynamicCommandExceptionType ERROR_NOTHING_FOUND = new DynamicCommandExceptionType(path -> Component.translatableEscape("arguments.nbtpath.nothing_found", new Object[] { path }));
/*  40 */   private static final DynamicCommandExceptionType ERROR_EXPECTED_LIST = new DynamicCommandExceptionType(node -> Component.translatableEscape("commands.data.modify.expected_list", new Object[] { node }));
/*  41 */   private static final DynamicCommandExceptionType ERROR_INVALID_INDEX = new DynamicCommandExceptionType(node -> Component.translatableEscape("commands.data.modify.invalid_index", new Object[] { node }));
/*     */   
/*     */   private static final char INDEX_MATCH_START = '[';
/*     */   private static final char INDEX_MATCH_END = ']';
/*     */   private static final char KEY_MATCH_START = '{';
/*     */   private static final char KEY_MATCH_END = '}';
/*     */   private static final char QUOTED_KEY_START = '"';
/*     */   private static final char SINGLE_QUOTED_KEY_START = '\'';
/*     */   
/*  50 */   public static NbtPathArgument nbtPath() { return new NbtPathArgument(); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static NbtPath getPath(CommandContext<CommandSourceStack> context, String name) { return (NbtPath)context.getArgument(name, NbtPath.class); }
/*     */ 
/*     */   
/*     */   public NbtPath parse(StringReader reader) throws CommandSyntaxException
/*     */   {
/*  59 */     List<Node> nodes = Lists.newArrayList();
/*  60 */     int start = reader.getCursor();
/*     */     
/*  62 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*  63 */     boolean firstNode = true;
/*  64 */     while (reader.canRead() && reader.peek() != ' ') {
/*  65 */       Node node = parseNode(reader, firstNode);
/*  66 */       nodes.add(node);
/*  67 */       object2IntOpenHashMap.put(node, reader.getCursor() - start);
/*  68 */       firstNode = false;
/*  69 */       if (reader.canRead()) {
/*  70 */         char next = reader.peek();
/*  71 */         if (next != ' ' && next != '[' && next != '{') {
/*  72 */           reader.expect('.');
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  77 */     return new NbtPath(reader.getString().substring(start, reader.getCursor()), (Node[])nodes.toArray(new Node[0]), object2IntOpenHashMap); } private static Node parseNode(StringReader reader, boolean firstNode) throws CommandSyntaxException { int index;
/*     */     CompoundTag pattern;
/*     */     CompoundTag pattern;
/*     */     int next;
/*  81 */     switch (reader.peek())
/*     */     { case '{':
/*  83 */         if (!firstNode) {
/*  84 */           throw ERROR_INVALID_NODE.createWithContext(reader);
/*     */         }
/*  86 */         pattern = TagParser.parseCompoundAsArgument(reader);
/*     */ 
/*     */       
/*     */       case '[':
/*  90 */         reader.skip();
/*  91 */         next = reader.peek();
/*     */         
/*  93 */         pattern = TagParser.parseCompoundAsArgument(reader);
/*  94 */         reader.expect(']');
/*     */ 
/*     */         
/*  97 */         reader.skip();
/*     */ 
/*     */ 
/*     */         
/* 101 */         index = reader.readInt();
/* 102 */         reader.expect(']');
/* 103 */         return (next == 123) ? new MatchElementNode(pattern) : ((next == 93) ? AllElementsNode.INSTANCE : new IndexedElementNode(index));
/*     */       case '"':
/*     */       case '\'':
/* 106 */        }  return readObjectNode(reader, readUnquotedName(reader)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Node readObjectNode(StringReader reader, String name) throws CommandSyntaxException {
/* 112 */     if (name.isEmpty()) {
/* 113 */       throw ERROR_INVALID_NODE.createWithContext(reader);
/*     */     }
/* 115 */     if (reader.canRead() && reader.peek() == '{') {
/* 116 */       CompoundTag pattern = TagParser.parseCompoundAsArgument(reader);
/* 117 */       return new MatchObjectNode(name, pattern);
/*     */     } 
/* 119 */     return new CompoundChildNode(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String readUnquotedName(StringReader reader) throws CommandSyntaxException {
/* 124 */     int start = reader.getCursor();
/* 125 */     while (reader.canRead() && isAllowedInUnquotedName(reader.peek())) {
/* 126 */       reader.skip();
/*     */     }
/* 128 */     if (reader.getCursor() == start) {
/* 129 */       throw ERROR_INVALID_NODE.createWithContext(reader);
/*     */     }
/* 131 */     return reader.getString().substring(start, reader.getCursor());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */ 
/*     */ 
/*     */   
/* 140 */   private static boolean isAllowedInUnquotedName(char c) { return (c != ' ' && c != '"' && c != '\'' && c != '[' && c != ']' && c != '.' && c != '{' && c != '}'); }
/*     */ 
/*     */   
/*     */   public static class NbtPath
/*     */   {
/*     */     private final String original;
/*     */     private final Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition;
/*     */     private final NbtPathArgument.Node[] nodes;
/* 148 */     public static final Codec<NbtPath> CODEC = Codec.STRING.comapFlatMap(string -> {
/*     */           
/*     */           try {
/* 151 */             NbtPath parsed = (new NbtPathArgument()).parse(new StringReader(string));
/* 152 */             return DataResult.success(parsed);
/* 153 */           } catch (CommandSyntaxException e) {
/* 154 */             return DataResult.error(());
/*     */           } 
/*     */         }NbtPath::asString);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     public static NbtPath of(String string) throws CommandSyntaxException { return (new NbtPathArgument()).parse(new StringReader(string)); }
/*     */ 
/*     */     
/*     */     public NbtPath(String original, Node[] nodes, Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition) {
/* 165 */       this.original = original;
/* 166 */       this.nodes = nodes;
/* 167 */       this.nodeToOriginalPosition = nodeToOriginalPosition;
/*     */     }
/*     */     
/*     */     public List<Tag> get(Tag tag) throws CommandSyntaxException {
/* 171 */       List<Tag> result = Collections.singletonList(tag);
/* 172 */       for (NbtPathArgument.Node node : this.nodes) {
/* 173 */         result = node.get(result);
/* 174 */         if (result.isEmpty()) {
/* 175 */           throw createNotFoundException(node);
/*     */         }
/*     */       } 
/* 178 */       return result;
/*     */     }
/*     */     
/*     */     public int countMatching(Tag tag) {
/* 182 */       List<Tag> result = Collections.singletonList(tag);
/* 183 */       for (NbtPathArgument.Node node : this.nodes) {
/* 184 */         result = node.get(result);
/* 185 */         if (result.isEmpty()) {
/* 186 */           return 0;
/*     */         }
/*     */       } 
/* 189 */       return result.size();
/*     */     }
/*     */     
/*     */     private List<Tag> getOrCreateParents(Tag tag) throws CommandSyntaxException {
/* 193 */       List<Tag> result = Collections.singletonList(tag);
/*     */       
/* 195 */       for (int i = 0; i < this.nodes.length - 1; i++) {
/* 196 */         NbtPathArgument.Node node = this.nodes[i];
/* 197 */         int next = i + 1;
/* 198 */         Objects.requireNonNull(this.nodes[next]); result = node.getOrCreate(result, this.nodes[next]::createPreferredParentTag);
/* 199 */         if (result.isEmpty()) {
/* 200 */           throw createNotFoundException(node);
/*     */         }
/*     */       } 
/* 203 */       return result;
/*     */     }
/*     */     
/*     */     public List<Tag> getOrCreate(Tag tag, Supplier<Tag> newTagValue) throws CommandSyntaxException {
/* 207 */       List<Tag> result = getOrCreateParents(tag);
/*     */       
/* 209 */       NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 210 */       return lastNode.getOrCreate(result, newTagValue);
/*     */     }
/*     */ 
/*     */     
/* 214 */     private static int apply(List<Tag> targets, Function<Tag, Integer> operation) { return ((Integer)targets.stream().map(operation).reduce(Integer.valueOf(0), (a, b) -> Integer.valueOf(a.intValue() + b.intValue()))).intValue(); }
/*     */ 
/*     */     
/*     */     public static boolean isTooDeep(Tag tag, int depth) {
/* 218 */       if (depth >= 512) {
/* 219 */         return true;
/*     */       }
/* 221 */       if (tag instanceof CompoundTag) { CompoundTag compound = (CompoundTag)tag;
/* 222 */         for (Tag child : compound.values()) {
/* 223 */           if (isTooDeep(child, depth + 1)) {
/* 224 */             return true;
/*     */           }
/*     */         }  }
/* 227 */       else if (tag instanceof ListTag) { ListTag list = (ListTag)tag;
/* 228 */         for (Tag listEntry : list) {
/* 229 */           if (isTooDeep(listEntry, depth + 1)) {
/* 230 */             return true;
/*     */           }
/*     */         }  }
/*     */       
/* 234 */       return false;
/*     */     }
/*     */     
/*     */     public int set(Tag tag, Tag toAdd) throws CommandSyntaxException {
/* 238 */       if (isTooDeep(toAdd, estimatePathDepth())) {
/* 239 */         throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */       }
/* 241 */       Tag firstCopy = toAdd.copy();
/* 242 */       List<Tag> result = getOrCreateParents(tag);
/* 243 */       if (result.isEmpty()) {
/* 244 */         return 0;
/*     */       }
/*     */       
/* 247 */       NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 248 */       MutableBoolean usedFirstCopy = new MutableBoolean(false);
/* 249 */       return apply(result, t -> Integer.valueOf(lastNode.setTag(t, ())));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     private int estimatePathDepth() { return this.nodes.length; }
/*     */ 
/*     */     
/*     */     public int insert(int index, CompoundTag target, List<Tag> toInsert) throws CommandSyntaxException {
/* 264 */       List<Tag> toInsertCopy = new ArrayList<Tag>(toInsert.size());
/* 265 */       for (Tag tag : toInsert) {
/* 266 */         Tag copy = tag.copy();
/* 267 */         toInsertCopy.add(copy);
/* 268 */         if (isTooDeep(copy, estimatePathDepth())) {
/* 269 */           throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */         }
/*     */       } 
/* 272 */       Collection<Tag> targets = getOrCreate(target, ListTag::new);
/*     */       
/* 274 */       int modifiedCount = 0;
/* 275 */       boolean usedFirst = false;
/* 276 */       for (Tag targetTag : targets) {
/* 277 */         CollectionTag targetList; if (targetTag instanceof CollectionTag) { targetList = (CollectionTag)targetTag; }
/* 278 */         else { throw NbtPathArgument.ERROR_EXPECTED_LIST.create(targetTag); }
/*     */ 
/*     */         
/* 281 */         boolean modified = false;
/* 282 */         int actualIndex = (index < 0) ? (targetList.size() + index + 1) : index;
/* 283 */         for (Tag sourceTag : toInsertCopy) {
/*     */           try {
/* 285 */             if (targetList.addTag(actualIndex, usedFirst ? sourceTag.copy() : sourceTag)) {
/* 286 */               actualIndex++;
/* 287 */               modified = true;
/*     */             } 
/* 289 */           } catch (IndexOutOfBoundsException e) {
/* 290 */             throw NbtPathArgument.ERROR_INVALID_INDEX.create(Integer.valueOf(actualIndex));
/*     */           } 
/*     */         } 
/* 293 */         usedFirst = true;
/* 294 */         modifiedCount += (modified ? 1 : 0);
/*     */       } 
/*     */       
/* 297 */       return modifiedCount;
/*     */     }
/*     */     
/*     */     public int remove(Tag tag) {
/* 301 */       List<Tag> result = Collections.singletonList(tag);
/*     */       
/* 303 */       for (int i = 0; i < this.nodes.length - 1; i++) {
/* 304 */         result = this.nodes[i].get(result);
/*     */       }
/*     */       
/* 307 */       NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 308 */       Objects.requireNonNull(lastNode); return apply(result, lastNode::removeTag);
/*     */     }
/*     */     
/*     */     private CommandSyntaxException createNotFoundException(NbtPathArgument.Node node) {
/* 312 */       int index = this.nodeToOriginalPosition.getInt(node);
/* 313 */       return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, index));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 318 */     public String toString() { return this.original; }
/*     */ 
/*     */ 
/*     */     
/* 322 */     public String asString() { return this.original; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 327 */   private static Predicate<Tag> createTagPredicate(CompoundTag pattern) { return tag -> NbtUtils.compareNbt(pattern, tag, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface Node
/*     */   {
/* 342 */     default List<Tag> get(List<Tag> tags) { return collect(tags, this::getTag); }
/*     */ 
/*     */ 
/*     */     
/* 346 */     default List<Tag> getOrCreate(List<Tag> tags, Supplier<Tag> child) { return collect(tags, (tag, output) -> getOrCreateTag(tag, child, output)); }
/*     */     void getTag(Tag param1Tag, List<Tag> param1List);
/*     */     
/*     */     default List<Tag> collect(List<Tag> tags, BiConsumer<Tag, List<Tag>> collector) {
/* 350 */       List<Tag> result = Lists.newArrayList();
/*     */       
/* 352 */       for (Tag tag : tags) {
/* 353 */         collector.accept(tag, result);
/*     */       }
/*     */       
/* 356 */       return result;
/*     */     }
/*     */     void getOrCreateTag(Tag param1Tag, Supplier<Tag> param1Supplier, List<Tag> param1List);
/*     */     Tag createPreferredParentTag();
/*     */     int setTag(Tag param1Tag, Supplier<Tag> param1Supplier);
/*     */     
/*     */     int removeTag(Tag param1Tag); }
/*     */   
/* 364 */   private static class CompoundChildNode implements Node { public CompoundChildNode(String name) { this.name = name; }
/*     */     
/*     */     private final String name;
/*     */     
/*     */     public void getTag(Tag parent, List<Tag> output) {
/* 369 */       if (parent instanceof CompoundTag) {
/* 370 */         Tag result = ((CompoundTag)parent).get(this.name);
/* 371 */         if (result != null) {
/* 372 */           output.add(result);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 379 */       if (parent instanceof CompoundTag) { Tag result; CompoundTag compound = (CompoundTag)parent;
/*     */         
/* 381 */         if (compound.contains(this.name)) {
/* 382 */           result = compound.get(this.name);
/*     */         } else {
/* 384 */           result = (Tag)child.get();
/* 385 */           compound.put(this.name, result);
/*     */         } 
/*     */         
/* 388 */         output.add(result); }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 394 */     public Tag createPreferredParentTag() { return new CompoundTag(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 399 */       if (parent instanceof CompoundTag) { CompoundTag compound = (CompoundTag)parent;
/* 400 */         Tag newValue = (Tag)toAdd.get();
/* 401 */         Tag previousValue = compound.put(this.name, newValue);
/* 402 */         if (!newValue.equals(previousValue)) {
/* 403 */           return 1;
/*     */         } }
/*     */ 
/*     */       
/* 407 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag parent) {
/* 412 */       if (parent instanceof CompoundTag) { CompoundTag compound = (CompoundTag)parent;
/* 413 */         if (compound.contains(this.name)) {
/* 414 */           compound.remove(this.name);
/* 415 */           return 1;
/*     */         }  }
/*     */ 
/*     */       
/* 419 */       return 0;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class IndexedElementNode
/*     */     implements Node {
/*     */     private final int index;
/*     */     
/* 427 */     public IndexedElementNode(int index) { this.index = index; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void getTag(Tag parent, List<Tag> output) {
/* 432 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 433 */         int size = list.size();
/* 434 */         int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/*     */         
/* 436 */         if (0 <= actualIndex && actualIndex < size) {
/* 437 */           output.add(list.get(actualIndex));
/*     */         } }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 444 */     public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) { getTag(parent, output); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 449 */     public Tag createPreferredParentTag() { return new ListTag(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 454 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 455 */         int size = list.size();
/* 456 */         int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/*     */         
/* 458 */         if (0 <= actualIndex && actualIndex < size) {
/* 459 */           Tag previousValue = list.get(actualIndex);
/* 460 */           Tag newValue = (Tag)toAdd.get();
/* 461 */           if (!newValue.equals(previousValue) && list.setTag(actualIndex, newValue)) {
/* 462 */             return 1;
/*     */           }
/*     */         }  }
/*     */ 
/*     */       
/* 467 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag parent) {
/* 472 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 473 */         int size = list.size();
/* 474 */         int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/*     */         
/* 476 */         if (0 <= actualIndex && actualIndex < size) {
/* 477 */           list.remove(actualIndex);
/* 478 */           return 1;
/*     */         }  }
/*     */ 
/*     */       
/* 482 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MatchElementNode implements Node {
/*     */     private final CompoundTag pattern;
/*     */     private final Predicate<Tag> predicate;
/*     */     
/*     */     public MatchElementNode(CompoundTag pattern) {
/* 491 */       this.pattern = pattern;
/* 492 */       this.predicate = NbtPathArgument.createTagPredicate(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag parent, List<Tag> output) {
/* 497 */       if (parent instanceof ListTag) { ListTag list = (ListTag)parent;
/* 498 */         Objects.requireNonNull(output); list.stream().filter(this.predicate).forEach(output::add); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 504 */       MutableBoolean foundAnything = new MutableBoolean();
/* 505 */       if (parent instanceof ListTag) { ListTag list = (ListTag)parent;
/* 506 */         list.stream().filter(this.predicate).forEach(t -> {
/* 507 */               output.add(t);
/* 508 */               foundAnything.setTrue();
/*     */             });
/*     */         
/* 511 */         if (foundAnything.isFalse()) {
/* 512 */           CompoundTag newTag = this.pattern.copy();
/* 513 */           list.add(newTag);
/* 514 */           output.add(newTag);
/*     */         }  }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 521 */     public Tag createPreferredParentTag() { return new ListTag(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 526 */       int changedCount = 0;
/* 527 */       if (parent instanceof ListTag) { ListTag list = (ListTag)parent;
/* 528 */         int size = list.size();
/* 529 */         if (size == 0) {
/* 530 */           list.add((Tag)toAdd.get());
/* 531 */           changedCount++;
/*     */         } else {
/* 533 */           for (int i = 0; i < size; i++) {
/* 534 */             Tag currentValue = list.get(i);
/* 535 */             if (this.predicate.test(currentValue)) {
/* 536 */               Tag newValue = (Tag)toAdd.get();
/* 537 */               if (!newValue.equals(currentValue) && list.setTag(i, newValue)) {
/* 538 */                 changedCount++;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }  }
/*     */ 
/*     */       
/* 545 */       return changedCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag parent) {
/* 550 */       int changedCount = 0;
/* 551 */       if (parent instanceof ListTag) { ListTag list = (ListTag)parent;
/* 552 */         for (int i = list.size() - 1; i >= 0; i--) {
/* 553 */           if (this.predicate.test(list.get(i))) {
/* 554 */             list.remove(i);
/* 555 */             changedCount++;
/*     */           } 
/*     */         }  }
/*     */ 
/*     */       
/* 560 */       return changedCount;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AllElementsNode
/*     */     implements Node
/*     */   {
/* 568 */     public static final AllElementsNode INSTANCE = new AllElementsNode();
/*     */ 
/*     */     
/*     */     public void getTag(Tag parent, List<Tag> output) {
/* 572 */       if (parent instanceof CollectionTag) { CollectionTag collection = (CollectionTag)parent;
/* 573 */         Iterables.addAll(output, collection); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 579 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 580 */         if (list.isEmpty()) {
/* 581 */           Tag result = (Tag)child.get();
/* 582 */           if (list.addTag(0, result)) {
/* 583 */             output.add(result);
/*     */           }
/*     */         } else {
/* 586 */           Iterables.addAll(output, list);
/*     */         }  }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 593 */     public Tag createPreferredParentTag() { return new ListTag(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 598 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 599 */         int size = list.size();
/* 600 */         if (size == 0) {
/* 601 */           list.addTag(0, (Tag)toAdd.get());
/* 602 */           return 1;
/*     */         } 
/* 604 */         Tag newValue = (Tag)toAdd.get();
/* 605 */         Objects.requireNonNull(newValue); int changedCount = size - (int)list.stream().filter(newValue::equals).count();
/* 606 */         if (changedCount == 0) {
/* 607 */           return 0;
/*     */         }
/* 609 */         list.clear();
/* 610 */         if (!list.addTag(0, newValue)) {
/* 611 */           return 0;
/*     */         }
/* 613 */         for (int i = 1; i < size; i++) {
/* 614 */           list.addTag(i, (Tag)toAdd.get());
/*     */         }
/*     */         
/* 617 */         return changedCount; }
/*     */ 
/*     */       
/* 620 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag parent) {
/* 625 */       if (parent instanceof CollectionTag) { CollectionTag list = (CollectionTag)parent;
/* 626 */         int size = list.size();
/* 627 */         if (size > 0) {
/* 628 */           list.clear();
/* 629 */           return size;
/*     */         }  }
/*     */ 
/*     */       
/* 633 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MatchObjectNode implements Node {
/*     */     private final String name;
/*     */     private final CompoundTag pattern;
/*     */     private final Predicate<Tag> predicate;
/*     */     
/*     */     public MatchObjectNode(String name, CompoundTag pattern) {
/* 643 */       this.name = name;
/* 644 */       this.pattern = pattern;
/* 645 */       this.predicate = NbtPathArgument.createTagPredicate(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag parent, List<Tag> output) {
/* 650 */       if (parent instanceof CompoundTag) {
/* 651 */         Tag result = ((CompoundTag)parent).get(this.name);
/* 652 */         if (this.predicate.test(result)) {
/* 653 */           output.add(result);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 660 */       if (parent instanceof CompoundTag) { CompoundTag compound = (CompoundTag)parent;
/* 661 */         CompoundTag compoundTag = compound.get(this.name);
/* 662 */         if (compoundTag == null) {
/* 663 */           compoundTag = this.pattern.copy();
/* 664 */           compound.put(this.name, compoundTag);
/* 665 */           output.add(compoundTag);
/* 666 */         } else if (this.predicate.test(compoundTag)) {
/* 667 */           output.add(compoundTag);
/*     */         }  }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 674 */     public Tag createPreferredParentTag() { return new CompoundTag(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 679 */       if (parent instanceof CompoundTag) { CompoundTag compound = (CompoundTag)parent;
/* 680 */         Tag currentValue = compound.get(this.name);
/* 681 */         if (this.predicate.test(currentValue)) {
/* 682 */           Tag newValue = (Tag)toAdd.get();
/* 683 */           if (!newValue.equals(currentValue)) {
/* 684 */             compound.put(this.name, newValue);
/* 685 */             return 1;
/*     */           } 
/*     */         }  }
/*     */ 
/*     */       
/* 690 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag parent) {
/* 695 */       if (parent instanceof CompoundTag) { CompoundTag compound = (CompoundTag)parent;
/* 696 */         Tag current = compound.get(this.name);
/* 697 */         if (this.predicate.test(current)) {
/* 698 */           compound.remove(this.name);
/* 699 */           return 1;
/*     */         }  }
/*     */ 
/*     */       
/* 703 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MatchRootObjectNode
/*     */     implements Node {
/*     */     private final Predicate<Tag> predicate;
/*     */     
/* 711 */     public MatchRootObjectNode(CompoundTag pattern) { this.predicate = NbtPathArgument.createTagPredicate(pattern); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void getTag(Tag self, List<Tag> output) {
/* 716 */       if (self instanceof CompoundTag && this.predicate.test(self)) {
/* 717 */         output.add(self);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 723 */     public void getOrCreateTag(Tag self, Supplier<Tag> child, List<Tag> output) { getTag(self, output); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 728 */     public Tag createPreferredParentTag() { return new CompoundTag(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 733 */     public int setTag(Tag parent, Supplier<Tag> toAdd) { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 738 */     public int removeTag(Tag parent) { return 0; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\NbtPathArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */