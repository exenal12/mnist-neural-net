To compile the program:
javac Main.java NeuralNetUtils/*.java

To prepare the network for Part 1:
- Press "3. Setup for Part 1", and enter "yes".
- Press "2. Network options", then "6. Toggle debug". This will show the weights, biases, and activations being calculated during training.
- Return to the menu and press "4. Start training the network".

Minibatch 1 will have X1 and X2, and Minibatch 2 will have X3 and X4. The Layer 2 activations for each case in Epoch 6 should match the expected outputs in the assignment sheet.

The testing data that's preloaded upon network setup is the same data that's used for training

I was trying to figure out File I/O before finishing part 1, but never got around to it, so FileIO.java is unused (and very unfinished)