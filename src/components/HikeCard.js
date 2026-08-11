import React from 'react';
import { View, Text, Pressable, StyleSheet } from 'react-native';

export default function HikeCard({ hike, onPress }) {
  return (
    <Pressable style={styles.card} onPress={onPress}>
      <Text style={styles.title}>{hike.name}</Text>
      <Text style={styles.details}>{hike.location} · {hike.distance} · {hike.difficulty}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  card: {
    padding: 16,
    marginBottom: 12,
    borderRadius: 12,
    backgroundColor: '#f2f2f2',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  details: {
    marginTop: 4,
    color: '#555',
  },
});
