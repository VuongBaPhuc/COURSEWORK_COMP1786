import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, ScrollView, Alert, TouchableOpacity } from 'react-native';
import { v4 as uuidv4 } from 'uuid';

const difficultyOptions = ['Easy', 'Medium', 'Hard'];

export default function AddHikeScreen({ navigation, onAddHike }) {
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const [distance, setDistance] = useState('');
  const [difficulty, setDifficulty] = useState('');
  const [description, setDescription] = useState('');

  const handleSave = () => {
    if (!name.trim() || !location.trim() || !distance.trim() || !difficulty.trim()) {
      Alert.alert('Missing information', 'Please fill in all required fields before saving.');
      return;
    }

    const newHike = {
      id: uuidv4(),
      name: name.trim(),
      location: location.trim(),
      distance: distance.trim(),
      difficulty: difficulty.trim(),
      description: description.trim(),
    };

    onAddHike(newHike);
    navigation.navigate('HikeList');
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.label}>Hike Name *</Text>
      <TextInput style={styles.input} value={name} onChangeText={setName} placeholder="Name" />
      <Text style={styles.label}>Location *</Text>
      <TextInput style={styles.input} value={location} onChangeText={setLocation} placeholder="Location" />
      <Text style={styles.label}>Distance *</Text>
      <TextInput style={styles.input} value={distance} onChangeText={setDistance} placeholder="E.g. 10km" />
      <Text style={styles.label}>Difficulty *</Text>
      <View style={styles.optionRow}>
        {difficultyOptions.map((option) => (
          <TouchableOpacity
            key={option}
            style={[
              styles.optionButton,
              difficulty === option && styles.optionButtonSelected,
            ]}
            onPress={() => setDifficulty(option)}
          >
            <Text style={[styles.optionText, difficulty === option && styles.optionTextSelected]}>{option}</Text>
          </TouchableOpacity>
        ))}
      </View>
      <Text style={styles.label}>Description</Text>
      <TextInput
        style={[styles.input, styles.textarea]}
        value={description}
        onChangeText={setDescription}
        placeholder="Detailed description"
        multiline
      />
      <View style={styles.buttonRow}>
        <View style={styles.buttonWrapper}>
          <Button title="Back" onPress={() => navigation.goBack()} />
        </View>
        <View style={styles.buttonWrapper}>
          <Button title="Save Hike" onPress={handleSave} />
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
  },
  label: {
    marginTop: 12,
    fontSize: 16,
    fontWeight: '600',
  },
  input: {
    marginTop: 8,
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 12,
    borderRadius: 8,
    backgroundColor: '#fff',
  },
  textarea: {
    minHeight: 100,
    textAlignVertical: 'top',
  },
  optionRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 8,
  },
  optionButton: {
    flex: 1,
    padding: 12,
    marginRight: 8,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 8,
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  optionButtonSelected: {
    backgroundColor: '#007bff',
    borderColor: '#007bff',
  },
  optionText: {
    color: '#333',
    fontWeight: '600',
  },
  optionTextSelected: {
    color: '#fff',
  },
  buttonRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 20,
  },
  buttonWrapper: {
    flex: 1,
    marginRight: 8,
  },
});
