import React from 'react';
import { View, Text, Button, StyleSheet, Alert, Platform } from 'react-native';

export default function HikeDetailScreen({ route, navigation, hikes, onDelete }) {
  const { hikeId } = route.params;
  const hike = hikes.find((item) => item.id === hikeId);

  if (!hike) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>Hike not found.</Text>
      </View>
    );
  }

  const handleDelete = () => {
    onDelete(hike.id);
    navigation.navigate('HikeList');
  };

  const confirmDelete = () => {
    if (Platform.OS === 'web' && typeof window !== 'undefined') {
      if (window.confirm('Are you sure you want to delete this hike?')) {
        handleDelete();
      }
      return;
    }

    Alert.alert('Confirm', 'Are you sure you want to delete this hike?', [
      { text: 'Cancel', style: 'cancel' },
      { text: 'Delete', style: 'destructive', onPress: handleDelete },
    ]);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>{hike.name}</Text>
      <Text style={styles.label}>Location:</Text>
      <Text style={styles.value}>{hike.location}</Text>
      <Text style={styles.label}>Distance:</Text>
      <Text style={styles.value}>{hike.distance}</Text>
      <Text style={styles.label}>Difficulty:</Text>
      <Text style={styles.value}>{hike.difficulty}</Text>
      <Text style={styles.label}>Description:</Text>
      <Text style={styles.value}>{hike.description}</Text>
      <View style={styles.buttonGroup}>
        <Button title="Edit" onPress={() => navigation.navigate('EditHike', { hikeId: hike.id })} />
        <Button title="Delete Hike" color="#d9534f" onPress={confirmDelete} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  title: {
    fontSize: 26,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  label: {
    fontSize: 16,
    marginTop: 12,
    fontWeight: '600',
  },
  value: {
    fontSize: 16,
    marginTop: 4,
  },
  buttonGroup: {
    marginTop: 32,
    gap: 12,
  },
  message: {
    flex: 1,
    textAlign: 'center',
    textAlignVertical: 'center',
    fontSize: 18,
  },
});
