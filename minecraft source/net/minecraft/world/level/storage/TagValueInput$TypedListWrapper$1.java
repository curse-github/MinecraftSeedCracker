package net.minecraft.world.level.storage;

import com.google.common.collect.AbstractIterator;
import java.util.ListIterator;

class null extends AbstractIterator<T> {
  protected T computeNext() { // Byte code:
    //   0: aload_0
    //   1: getfield val$iterator : Ljava/util/ListIterator;
    //   4: invokeinterface hasNext : ()Z
    //   9: ifeq -> 162
    //   12: aload_0
    //   13: getfield val$iterator : Ljava/util/ListIterator;
    //   16: invokeinterface nextIndex : ()I
    //   21: istore_1
    //   22: aload_0
    //   23: getfield val$iterator : Ljava/util/ListIterator;
    //   26: invokeinterface next : ()Ljava/lang/Object;
    //   31: checkcast net/minecraft/nbt/Tag
    //   34: astore_2
    //   35: aload_0
    //   36: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
    //   39: getfield codec : Lcom/mojang/serialization/Codec;
    //   42: aload_0
    //   43: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
    //   46: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
    //   49: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
    //   52: aload_2
    //   53: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
    //   58: dup
    //   59: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
    //   62: pop
    //   63: astore_3
    //   64: iconst_0
    //   65: istore #4
    //   67: aload_3
    //   68: iload #4
    //   70: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
    //   75: lookupswitch default -> 100, 0 -> 110, 1 -> 122
    //   100: new java/lang/MatchException
    //   103: dup
    //   104: aconst_null
    //   105: aconst_null
    //   106: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   109: athrow
    //   110: aload_3
    //   111: checkcast com/mojang/serialization/DataResult$Success
    //   114: astore #5
    //   116: aload #5
    //   118: invokevirtual value : ()Ljava/lang/Object;
    //   121: areturn
    //   122: aload_3
    //   123: checkcast com/mojang/serialization/DataResult$Error
    //   126: astore #6
    //   128: aload_0
    //   129: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
    //   132: iload_1
    //   133: aload_2
    //   134: aload #6
    //   136: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
    //   139: aload #6
    //   141: invokevirtual partialValue : ()Ljava/util/Optional;
    //   144: invokevirtual isPresent : ()Z
    //   147: ifeq -> 159
    //   150: aload #6
    //   152: invokevirtual partialValue : ()Ljava/util/Optional;
    //   155: invokevirtual get : ()Ljava/lang/Object;
    //   158: areturn
    //   159: goto -> 0
    //   162: aload_0
    //   163: invokevirtual endOfData : ()Ljava/lang/Object;
    //   166: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #341	-> 0
    //   #342	-> 12
    //   #343	-> 22
    //   #344	-> 35
    //   #345	-> 110
    //   #346	-> 116
    //   #348	-> 122
    //   #349	-> 128
    //   #350	-> 139
    //   #351	-> 150
    //   #355	-> 159
    //   #356	-> 162
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success;
    //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error;
    //   22	137	1	index	I
    //   35	124	2	value	Lnet/minecraft/nbt/Tag;
    //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1;
    // Local variable type table:
    //   start	length	slot	name	signature
    //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
    //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
    //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1; }
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\TagValueInput$TypedListWrapper$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */